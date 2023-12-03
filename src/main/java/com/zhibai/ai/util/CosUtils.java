package com.zhibai.ai.util;

import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.qcloud.cos.transfer.Upload;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 腾讯云对象存储工具类
 */
@Component
public class CosUtils implements DisposableBean {

    //腾讯云的SecretId
    @Value("${cos.secretId}")
    private String secretId;

    //腾讯云的SecretKey
    @Value("${cos.secretKey}")
    private String secretKey;

    //腾讯云的bucket (存储桶)
    @Value("${cos.bucket}")
    private String bucket;

    //腾讯云的region(bucket所在地区)
    @Value("${cos.region}")
    private String region;

    //腾讯云的访问基础链接:
    @Value("${cos.url}")
    private String baseUrl;

    private COSClient cosClient;

    @PostConstruct
    private void init() {
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置bucket的区域
        ClientConfig clientconfig = new ClientConfig(new Region(region));
        // 3 生成cos客户端
        cosClient = new COSClient(cred, clientconfig);
    }

    @Override
    public void destroy() throws Exception {
        cosClient.shutdown();
    }

    static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 文件上传
     *
     * @param file    文件
     * @param fileDir 桶中文件的路径
     * @return
     */
    public String uploadImageFile(MultipartFile file, String fileDir) {

        fileDir = getCorrectFolderPath(fileDir);

        String fileName = String.format("%s.%s", UuidUtils.getUUID(), "jpg");

        try (InputStream inputStream = file.getInputStream()) {
            // 从输入流上传(需提前告知输入流的长度, 否则可能导致 oom)
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 设置输入流长度
            objectMetadata.setContentLength(inputStream.available());
            // 设置 Content type, 默认是 application/octet-stream
            objectMetadata.setContentType(getContentType(getExtension(file.getOriginalFilename())));
            // 设置不缓存
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            // inline在线预览,中文乱码已处理,下载文件的时候可以用原来上传的名字
            objectMetadata.setContentDisposition("inline;filename=" + URLEncoder.encode(file.getOriginalFilename(), "utf-8"));
            cosClient.putObject(bucket, fileDir + fileName, inputStream, objectMetadata);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baseUrl + fileDir + fileName;
    }

    /**
     * 文件批量上传
     *
     * @param files
     * @param fileDir
     * @return
     */
    public String batchUploadFile(MultipartFile[] files, String fileDir) {

        //处理文件夹路径前无/后加/
        fileDir = getCorrectFolderPath(fileDir);

        String resultUrls = "";
        try {

            for (MultipartFile file : files) {
                String fileName = String.format("%s.%s", UUID.randomUUID().toString().replace("-", ""), getExtension(file.getOriginalFilename()));

                InputStream inputStream = file.getInputStream();
                // 从输入流上传(需提前告知输入流的长度, 否则可能导致 oom)
                ObjectMetadata objectMetadata = new ObjectMetadata();
                // 设置输入流长度为500
                objectMetadata.setContentLength(inputStream.available());
                // 设置 Content type, 默认是 application/octet-stream
                objectMetadata.setContentType(getContentType(getExtension(file.getOriginalFilename())));
                // 设置不缓存
                objectMetadata.setCacheControl("no-cache");
                objectMetadata.setHeader("Pragma", "no-cache");
                // inline在线预览,中文乱码已处理,下载文件的时候可以用原来上传的名字
                objectMetadata.setContentDisposition("inline;filename=" + URLEncoder.encode(file.getOriginalFilename(), "utf-8"));
                cosClient.putObject(bucket, fileDir + fileName, inputStream, objectMetadata);

                resultUrls = resultUrls + "," + baseUrl + fileDir + fileName;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultUrls.substring(1);
    }

    public TransferManager cosTransferManager() {

        // 线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);
        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(cosClient, threadPool);
        // 设置高级接口的分块上传阈值和分块大小为10MB
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(10 * 1024 * 1024);
        transferManagerConfiguration.setMinimumUploadPartSize(10 * 1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);
        return transferManager;
    }

    public Upload upload(String filePath, MultipartFile file) {
        TransferManager manager = cosTransferManager();
        Upload result = null;
        try (InputStream inputStream = file.getInputStream()) {
            result = manager.upload(bucket, filePath, inputStream, new ObjectMetadata());
            result.waitForUploadResult();
        } catch (CosClientException | InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            manager.shutdownNow();
        }
        return result;
    }

    public Download download(String filePath, File destFile) {
        TransferManager manager = cosTransferManager();
        Download download = null;
        try {
            download = manager.download(bucket, filePath, destFile);
            download.waitForCompletion();
        } catch (CosClientException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            manager.shutdownNow();
        }
        return download;
    }

    /**
     * 获取对象键
     * 官方文档：腾讯云对象存储 COS 中的对象需具有合法的对象键，对象键（ObjectKey）是对象在存储桶中的唯一标识。
     * 例如：在对象的访问地址examplebucket-1250000000.cos.ap-guangzhou.myqcloud.com/folder/picture.jpg 中，对象键为folder/picture.jpg。
     *
     * @param fileUrl
     * @return
     */
    private static String getKeyByFileURL(String fileUrl) {
        //获取完整路径中的文件名，截去"http://oss-cn-shenzhen.aliyuncs.com/"剩下的就是文件名
        if (fileUrl.indexOf("http") > -1) {
            return fileUrl.substring(fileUrl.indexOf("/", 9) + 1);
        }
        return fileUrl;
    }

    /**
     * 处理正确文件夹路径(前无/后加/)
     * 例如：/upload/image -> upload/image/
     *
     * @param fileDir
     * @return
     */
    private static String getCorrectFolderPath(String fileDir) {
        //处理文件夹路径前无/后加/
        if (StrUtil.isEmpty(fileDir)) {
            fileDir = "";
        } else {
            fileDir = (fileDir.indexOf("/") == 0) ? fileDir.substring(1) : fileDir;
            fileDir = (fileDir.lastIndexOf("/") == fileDir.length() - 1) ? fileDir : fileDir + "/";
        }
        return fileDir;
    }

    /**
     * 判断COS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return ContentType
     */
    private static String getContentType(String FilenameExtension) {

        //image/jpg 可以在线预览
        if (FilenameExtension.equalsIgnoreCase("gif")
                || FilenameExtension.equalsIgnoreCase("jpeg")
                || FilenameExtension.equalsIgnoreCase("jpg")
                || FilenameExtension.equalsIgnoreCase("png")) {
            return "image/jpg";
        }
        if (FilenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase("pptx") ||
                FilenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase("docx") ||
                FilenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase("pdf")) {
            return "application/pdf";
        }
        if (FilenameExtension.equalsIgnoreCase("ppt")) {
            return "application/x-ppt";
        }
        if (FilenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        if (FilenameExtension.equalsIgnoreCase("mp3")) {
            return "audio/mp3";
        }
        if (FilenameExtension.equalsIgnoreCase("mp4")) {
            return "video/mp4";
        }
        if (FilenameExtension.equalsIgnoreCase("avi")) {
            return "video/avi";
        }
        if (FilenameExtension.equalsIgnoreCase("wmv")) {
            return "video/x-ms-wmv";
        }
        return "image/jpg";
    }

}



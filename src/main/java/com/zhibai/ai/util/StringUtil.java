package com.zhibai.ai.util;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author: kanson
 * @desc:
 * @version: 1.0
 * @time: 2023/05/26 23:39
 */
public class StringUtil {

    public static String getExceptionStackTrace(Exception exception) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exception.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }

    // 获取域名
    public static String getDomainName(HttpServletRequest httpServletRequest) {
        StringBuffer testUrl = httpServletRequest.getRequestURL();
        return testUrl.delete(testUrl.length() - httpServletRequest.getRequestURI().length(), testUrl.length()).toString().replace("https://", "").replace("http://", "");
    }

    public static String sha1Encrypt(String str) {
        try {
            //获取加密对象
            MessageDigest md = MessageDigest.getInstance("sha1");
            //加密
            byte[] digest = md.digest(str.getBytes());
            //sha1加密后结果只能是如下数组的数值
            char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            //拼接加密后的字符串
            StringBuffer sb = new StringBuffer();
            //处理加密结果
            for (byte b : digest) {
                //1byte代表8位（二进制），分别对高四位和低四位进行与运算，得到chas对应的数值
                sb.append(chars[(b >> 4) & 15]);
                sb.append(chars[b & 15]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}

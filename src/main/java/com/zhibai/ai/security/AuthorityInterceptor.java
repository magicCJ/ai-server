package com.zhibai.ai.security;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zhibai.ai.common.AiServerException;
import com.zhibai.ai.common.AiServerExceptionEnum;
import com.zhibai.ai.model.Result;
import com.zhibai.ai.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author xunbai
 * @Date 2023-05-13 17:43
 * @description
 **/

@Slf4j
@Component
@Aspect
public class AuthorityInterceptor {

    @Autowired
    private  JwtTokenProvider tokenProvider;

    private static final String NOTIFY = "notify";

    private static final List<String> STREAM_METHOD = Lists.newArrayList("start", "ask");

    @Pointcut("execution(public * com.xunbai.ai.controller..*.*(..)) && !within(com.zhibai.ai.controller.HealthController) && !within(com.zhibai.ai.controller.UserController) && !within(com.zhibai.ai.controller.StreamController)")
    public void pointcut(){

    }

    @Around("pointcut()")
    public Object doInvoke(ProceedingJoinPoint pjp){
        try {
            // MJ回调接口无需验签
            Signature signature = pjp.getSignature();
            if (NOTIFY.equals(signature.getName())){
                return pjp.proceed();
            }
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = sra.getRequest();

            String token = request.getHeader("token");
            if (StringUtils.isNotBlank(token) && tokenProvider.validateToken(token)) {
                Long userId = tokenProvider.getUserIdFromToken(token);
                UserThreadLocal.setUserId(userId);

                return pjp.proceed();
            } else {
                if (STREAM_METHOD.contains(signature.getName())){
                    sra.getResponse().setCharacterEncoding("UTF-8");
                    PrintWriter writer = sra.getResponse().getWriter();
                    writer.write("{\n" +
                            "    \"errorCode\": \"1000\",\n" +
                            "    \"errorMessage\": \"登录已过期请重新登录!\",\n" +
                            "    \"success\": false,\n" +
                            "    \"data\": null\n" +
                            "}");
                    writer.flush();
                    writer.close();
                }
                return Result.failed(AiServerExceptionEnum.SECURITY_ERROR);
            }
        } catch (AiServerException e){
            log.error(getMethod(pjp) + " check false, param={}, message={}", JSON.toJSONString(pjp.getArgs()), e);
            return Result.failed(e.getCode(), e.getMessage());
        } catch (Throwable e){
            log.error(StringUtil.getExceptionStackTrace((Exception) e));
            printError(pjp, e);
            return Result.failed(AiServerExceptionEnum.SYSTEM_ERROR);
        } finally {
            // 清除本地线程用户ID
            UserThreadLocal.remove();
        }
    }

    private void printError(ProceedingJoinPoint pjp, Throwable e){
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(stream));
            // 请求参数
            Object[] args = pjp.getArgs();
            List<Object> argsList = new ArrayList<>();
            for (Object arg : args) {
                // 如果参数类型是请求和响应的http，则不需要拼接【这两个参数，使用JSON.toJSONString()转换会抛异常】
                if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                    continue;
                }
                argsList.add(arg);
            }
            log.error(getMethod(pjp) + " error, param={}, error={}", JSON.toJSONString(argsList), e);
            try{
                stream.close();
            }catch (IOException ioException){
                log.error("AuthorityInterceptor#closeStream#failed", ioException);
            }
        } catch (Exception exception) {
            log.error(StringUtil.getExceptionStackTrace(exception));
        }
    }

    public String getMethod(ProceedingJoinPoint pjp){
        Signature signature = pjp.getSignature();
        if (signature == null){
            return "NO_METHOD";
        }
        return "[" +
                signature.getDeclaringTypeName() +
                "#" +
                signature.getName() +
                "]";
    }

}

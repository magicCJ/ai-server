package com.zhibai.ai.security;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Component
@Slf4j
public class RequestWrapperFilter extends OncePerRequestFilter {

    private static final String[] excludedUrls = {"/scene/start/sse", "/scene/ask/sse"};

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String url = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return Stream.of(excludedUrls).anyMatch(x -> pathMatcher.match(x, url));
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse, @NotNull FilterChain filterChain) throws ServletException, IOException {
        Stopwatch begin = Stopwatch.createStarted();
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpServletResponse);
        try {
            try {
                log.info("请求开始: {}, Param:{}", wrappedRequest.getRequestURI(), JSON.toJSON(wrappedRequest.getParameterMap()));
            } catch (Exception var21) {
                log.error("[请求过滤前报错]request_before_error:method:[{}],contentType:[{}],url:[{}],error:{}", wrappedRequest.getMethod(), wrappedRequest.getContentType(), wrappedRequest.getRequestURI(), var21);
            }
            filterChain.doFilter(wrappedRequest, wrappedResponse);
            String[] result = printFullUrl(wrappedRequest);

            try {
                String fullUrl = result[0] == null ? "" : result[0];
                long ms = begin.elapsed(TimeUnit.MILLISECONDS);
                log.info("【请求结束返回】request_final_return:url:[{}],headers:{},end_of_ms:{}", fullUrl, responseHeader(httpServletResponse), ms);
            } catch (Exception var19) {
                log.error("【请求过滤后报错】request_after_error:method:[{}],contentType:[{}],url:[{}],error:{}", wrappedRequest.getMethod(), wrappedRequest.getContentType(), wrappedRequest.getRequestURI(), var19);
            }
        } finally {
            wrappedResponse.copyBodyToResponse();
        }
    }

    private String[] printFullUrl(ContentCachingRequestWrapper req) throws IOException {
        String parameterString = "";
        String body = "";
        parameterString = req.getRequestURL() + "?" + this.appendTo(req.getParameterMap().entrySet().iterator());

        body = new String(req.getContentAsByteArray(), req.getCharacterEncoding());
        log.info("【请求详情重放细节】request_detail_body_exists:method:[{}],contentType:[{}],url:[{}],body:[{}]", req.getMethod(), req.getContentType(), parameterString, body);
        return new String[]{parameterString, body};

    }

    private Map<Object, Object> responseHeader(HttpServletResponse res) {
        Iterator<String> headerNames = res.getHeaderNames().iterator();
        Map<Object, Object> headerMap = Maps.newHashMap();

        while (headerNames.hasNext()) {
            String key = headerNames.next();
            Iterator<String> n = res.getHeaders(key).iterator();

            String headerValue;
            String v;
            for (headerValue = ""; n.hasNext(); headerValue = headerValue + ":" + v) {
                v = n.next();
            }

            headerMap.put(key, headerValue);
        }

        return headerMap;
    }

    private String appendTo(Iterator<Map.Entry<String, String[]>> parts) {
        StringBuilder appendable = new StringBuilder();
        if (parts.hasNext()) {
            Map.Entry<String, String[]> outer = parts.next();
            appendable.append(outer.getKey()).append("=").append(((String[]) outer.getValue())[0]);

            while (parts.hasNext()) {
                Map.Entry<String, String[]> inner = parts.next();
                appendable.append("&");
                appendable.append(inner.getKey());
                appendable.append("=");
                appendable.append(((String[]) inner.getValue())[0]);
            }
        }

        return appendable.toString();
    }

}

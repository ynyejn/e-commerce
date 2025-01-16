package kr.hhplus.be.server.support.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Request, Response를 ContentCachingWrapper로 감싸서 content를 여러번 읽을 수 있도록 함
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        // 먼저 실제 요청 처리
        chain.doFilter(requestWrapper, responseWrapper);

        // 그 다음 Request 로깅 (이제 content를 읽을 수 있음)
        if (!requestWrapper.getMethod().equals("GET")) {
            String requestBody = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
            log.info("Request: {} {} body={}", requestWrapper.getMethod(), requestWrapper.getRequestURI(), requestBody);
        } else {
            log.info("Request: {} {}", requestWrapper.getMethod(), requestWrapper.getRequestURI());
        }

        // Response 로깅
        String responseBody = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        log.info("Response: {} {} body={}", requestWrapper.getMethod(), requestWrapper.getRequestURI(), responseBody);

        responseWrapper.copyBodyToResponse();
        MDC.clear();
    }

}
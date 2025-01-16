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
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);  // 요청 ID 설정

        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            // request 로깅
            String requestBody = getRequestBody(httpRequest);
            log.info("Request: {} {} body={}",
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI(),
                    requestBody
            );

            // Response를 캡처하기 위한 래퍼
            ContentCachingResponseWrapper responseWrapper =
                    new ContentCachingResponseWrapper(httpResponse);

            // 실제 요청 처리
            chain.doFilter(request, responseWrapper);

            // response 로깅
            String responseBody = getResponseBody(responseWrapper);
            log.info("Response: {} {} body={}",
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI(),
                    responseBody
            );

            // 응답 데이터 복원
            responseWrapper.copyBodyToResponse();

        } finally {
            MDC.clear();  // MDC 정리
        }
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        // POST, PUT 등의 요청에만 바디를 읽음
        if (!isReadableHttpMethod(request.getMethod())) {
            return "";
        }

        ContentCachingRequestWrapper requestWrapper =
            new ContentCachingRequestWrapper(request);
        return new String(requestWrapper.getContentAsByteArray());
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] buf = response.getContentAsByteArray();
        return new String(buf, StandardCharsets.UTF_8);
    }

    private boolean isReadableHttpMethod(String method) {
        return method.equals("POST") || method.equals("PUT") || 
               method.equals("PATCH") || method.equals("DELETE");
    }
}
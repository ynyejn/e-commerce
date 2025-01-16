package kr.hhplus.be.server.support.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "kr.hhplus.be.server.interfaces")
@Slf4j
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiCustomException(
            ApiException ex,
            HttpServletRequest request
    ) {
        log.error("Response: {} {} body={}",
                request.getMethod(),
                request.getRequestURI(),
                new ErrorResponse(ex.getApiErrorCode().name(), ex.getMessage())
        );

        return ResponseEntity.status(ex.getApiErrorCode().getHttpStatus())
                .body(new ErrorResponse(ex.getApiErrorCode().name(), ex.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception e,
            HttpServletRequest request
    ) {
        log.error("Response: {} {} body={}",
                request.getMethod(),
                request.getRequestURI(),
                new ErrorResponse("500", "에러가 발생했습니다."),
                e  // 스택트레이스
        );

        return ResponseEntity.status(500)
                .body(new ErrorResponse("500", "에러가 발생했습니다."));
    }
}

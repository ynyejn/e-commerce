package kr.hhplus.be.server.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "리소스를 찾을 수 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "유효하지 않은 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "접근 권한이 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, "CONFLICT", "리소스 충돌이 발생했습니다."),
    INSUFFICIENT_STOCK(HttpStatus.CONFLICT, "INSUFFICIENT_STOCK", "재고가 부족합니다."),
    INSUFFICIENT_COUPON(HttpStatus.CONFLICT, "INSUFFICIENT_COUPON", "쿠폰 수량이 부족합니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ApiErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}

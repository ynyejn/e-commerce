package kr.hhplus.be.server.domain.constant;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("결제 대기"),
    PAID("결제 완료"),
    CANCELLED("주문 취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}

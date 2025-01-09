package kr.hhplus.be.server.interfaces.order.dto.response;

import kr.hhplus.be.server.application.order.dto.result.OrderResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        String orderNo,
        String status,
        BigDecimal totalAmount,
        int totalQuantity,
        LocalDateTime createdAt
) {
    public static OrderResponse from(OrderResult orderResult) {
        return new OrderResponse(
                orderResult.orderId(),
                orderResult.orderNo(),
                orderResult.status(),
                orderResult.totalAmount(),
                orderResult.totalQuantity(),
                orderResult.createdAt()
        );
    }
}

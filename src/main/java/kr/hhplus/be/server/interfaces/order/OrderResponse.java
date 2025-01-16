package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.domain.order.OrderInfo;

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

    public static OrderResponse from(OrderInfo orderInfo) {
        return new OrderResponse(
                orderInfo.orderId(),
                orderInfo.orderNo(),
                orderInfo.status(),
                orderInfo.totalAmount(),
                orderInfo.totalQuantity(),
                orderInfo.createdAt()
        );
    }
}

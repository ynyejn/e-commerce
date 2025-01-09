package kr.hhplus.be.server.interfaces.order.dto.response;

import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;

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

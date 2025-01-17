package kr.hhplus.be.server.application.order.dto.result;

import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResult(
        Long orderId,
        String orderNo,
        String status,
        BigDecimal totalAmount,
        Integer totalQuantity,
        LocalDateTime createdAt,
        LocalDateTime paidAt
) {
    public static OrderResult from(OrderInfo orderInfo) {
        return new OrderResult(
                orderInfo.orderId(),
                orderInfo.orderNo(),
                orderInfo.status(),
                orderInfo.totalAmount(),
                orderInfo.totalQuantity(),
                orderInfo.createdAt(),
                orderInfo.paidAt()
        );
    }
}

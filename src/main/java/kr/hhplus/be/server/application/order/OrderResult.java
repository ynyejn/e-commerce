package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public record OrderResult(
        Long orderId,
        String orderNo,
        String status,
        BigDecimal paymentAmount,
        BigDecimal totalAmount,
        Integer totalQuantity,
        LocalDateTime createdAt
) {
    public static OrderResult from(OrderInfo orderInfo) {
        return new OrderResult(
                orderInfo.orderId(),
                orderInfo.orderNo(),
                orderInfo.status(),
                orderInfo.paymentAmount().setScale(2, RoundingMode.HALF_UP),
                orderInfo.totalAmount().setScale(2, RoundingMode.HALF_UP),
                orderInfo.totalQuantity(),
                orderInfo.createdAt()
        );
    }
}

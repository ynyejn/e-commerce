package kr.hhplus.be.server.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderConfirmedEvent(
        Long orderId,
        Long userId,
        Long paymentId,
        BigDecimal amount,
        LocalDateTime paidAt
) {
    public static OrderConfirmedEvent from(Order order) {
        return new OrderConfirmedEvent(
                order.getId(),
                order.getUser().getId(),
                order.getId(),
                order.getPaymentAmount(),
                order.getCreatedAt()
        );
    }
}

package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.payment.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderEvent(
        Long orderId,
        Long userId,
        Long paymentId,
        BigDecimal amount,
        LocalDateTime paidAt
) {
    public static OrderEvent from(Order order) {
        return new OrderEvent(
                order.getId(),
                order.getUser().getId(),
                order.getId(),
                order.getPaymentAmount(),
                order.getCreatedAt()
        );
    }
}

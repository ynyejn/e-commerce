package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.domain.order.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentEvent(
        Long orderId,
        Long userId,
        Long paymentId,
        BigDecimal amount,
        LocalDateTime paidAt
) {
    public static PaymentEvent from(Payment payment) {
        return new PaymentEvent(
                payment.getOrder().getId(),
                payment.getOrder().getUser().getId(),
                payment.getId(),
                payment.getPaymentAmount(),
                payment.getCreatedAt()
        );
    }
}

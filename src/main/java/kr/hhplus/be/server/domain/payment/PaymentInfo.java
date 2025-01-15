package kr.hhplus.be.server.domain.payment;

import java.math.BigDecimal;

public record PaymentInfo(
        Long paymentId,
        Long orderId,
        BigDecimal paymentAmount
) {
    public static PaymentInfo from(Payment payment) {
        return new PaymentInfo(payment.getId(), payment.getOrderId(), payment.getPaymentAmount());
    }
}

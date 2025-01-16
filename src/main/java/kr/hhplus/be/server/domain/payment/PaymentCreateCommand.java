package kr.hhplus.be.server.domain.payment;

import java.math.BigDecimal;

public record PaymentCreateCommand(
        Long orderId,
        BigDecimal paymentAmount
) {
    public static PaymentCreateCommand from(Long orderId, BigDecimal paymentAmount) {
        return new PaymentCreateCommand(orderId, paymentAmount);
    }
}

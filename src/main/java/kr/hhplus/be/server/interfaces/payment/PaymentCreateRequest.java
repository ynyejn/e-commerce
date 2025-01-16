package kr.hhplus.be.server.interfaces.payment;

import kr.hhplus.be.server.domain.payment.PaymentCreateCommand;

import java.math.BigDecimal;

public record PaymentCreateRequest(
        Long orderId,
        BigDecimal paymentAmount
) {
    public PaymentCreateCommand toCommand() {
        return new PaymentCreateCommand(orderId, paymentAmount);
    }
}

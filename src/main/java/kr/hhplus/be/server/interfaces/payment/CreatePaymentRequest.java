package kr.hhplus.be.server.interfaces.payment;

import kr.hhplus.be.server.domain.payment.PaymentCreateCommand;

public record CreatePaymentRequest(
        Long orderId
) {
    public PaymentCreateCommand toCommand() {
        return new PaymentCreateCommand(this.orderId());
    }
}

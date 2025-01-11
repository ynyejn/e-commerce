package kr.hhplus.be.server.interfaces.order.dto.request;

import kr.hhplus.be.server.domain.order.dto.command.PaymentCreateCommand;

public record CreatePaymentRequest(
        Long orderId
) {
    public PaymentCreateCommand toCommand() {
        return new PaymentCreateCommand(this.orderId());
    }
}

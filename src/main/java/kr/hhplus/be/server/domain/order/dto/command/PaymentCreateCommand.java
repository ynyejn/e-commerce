package kr.hhplus.be.server.domain.order.dto.command;

public record PaymentCreateCommand(
        Long orderId
) {
}

package kr.hhplus.be.server.domain.order.dto.command;

public record PaymentCreateCommand(
        Long orderId
) {
    public static PaymentCreateCommand from(Long orderId) {
        return new PaymentCreateCommand(orderId);
    }
}

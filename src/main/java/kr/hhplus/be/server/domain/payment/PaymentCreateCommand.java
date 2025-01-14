package kr.hhplus.be.server.domain.payment;

public record PaymentCreateCommand(
        Long orderId
) {
    public static PaymentCreateCommand from(Long orderId) {
        return new PaymentCreateCommand(orderId);
    }
}

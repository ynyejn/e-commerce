package kr.hhplus.be.server.domain.order;

public record OrderConfirmCommand(
        Long orderId
) {
    public static OrderConfirmCommand from(Long orderId) {
        return new OrderConfirmCommand(orderId);
    }
}

package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.support.DomainEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderCompletedEvent(
        Long orderId,
        Long userId,
        Long paymentId,
        BigDecimal amount,
        LocalDateTime paidAt,
        LocalDateTime createdAt
) implements DomainEvent {

    public static OrderCompletedEvent from(Order order) {
        return new OrderCompletedEvent(
                order.getId(),
                order.getUser().getId(),
                order.getId(),
                order.getPaymentAmount(),
                order.getCreatedAt(),
                LocalDateTime.now()
        );
    }

    @Override
    public String eventType() {
        return this.getClass().getSimpleName();
    }

    @Override
    public Long entityId() {
        return orderId;
    }
}
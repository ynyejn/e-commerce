package kr.hhplus.be.server.domain.order.dto.info;

import kr.hhplus.be.server.domain.order.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderInfo(
        Long orderId,
        String orderNumber,
        String status,
        BigDecimal amount,
        Integer quantity,
        LocalDateTime createdAt,
        LocalDateTime paidAt
) {
    public static OrderInfo from(Order order) {
        return new OrderInfo(
                order.getId(),
                order.getOrderNo(),
                order.getStatus().getDescription(),
                order.getPaymentAmount(),
                order.getTotalQuantity(),
                order.getCreatedAt(),
                order.getPaidAt()
        );
    }
}

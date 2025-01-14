package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderCreateCommand;

import java.util.List;

public record OrderCreateCriteria(
        Long userId,
        List<OrderItemCriteria> products,
        Long couponIssueId
) {
    public OrderCreateCommand toOrderCommand() {
        return new OrderCreateCommand(
                userId,
                products.stream()
                        .map(product -> new OrderCreateCommand.OrderItemCommand(
                                product.productId(),
                                product.quantity()
                        ))
                        .toList(),
                couponIssueId
        );
    }


    public record OrderItemCriteria(
            Long productId,
            int quantity
    ) {
    }
}
package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderCreateCommand;
import kr.hhplus.be.server.domain.product.ValidatedProductInfo;

import java.util.List;

public record OrderCreateCriteria(
        List<OrderItemCriteria> products,
        Long couponIssueId
) {

    public List<OrderCreateCommand.OrderItemCommand> toOrderItemCommands() {
        return products.stream()
                .map(product -> new OrderCreateCommand.OrderItemCommand(
                        product.productId(),
                        null,
                        product.quantity()
                ))
                .toList();
    }

    public OrderCreateCommand toOrderCommand(List<ValidatedProductInfo> validateProducts) {
        return new OrderCreateCommand(
                validateProducts.stream()
                        .map(product -> new OrderCreateCommand.OrderItemCommand(
                                product.product().getId(),
                                product.product(),
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

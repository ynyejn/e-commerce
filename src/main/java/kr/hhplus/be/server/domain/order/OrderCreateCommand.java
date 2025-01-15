package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;

import java.util.List;

public record OrderCreateCommand(
        List<OrderItemCommand> products,
        Long couponIssueId
        ) {

    public record OrderItemCommand(
            Long productId,
            Product product,
            int quantity
    ) {
    }

}

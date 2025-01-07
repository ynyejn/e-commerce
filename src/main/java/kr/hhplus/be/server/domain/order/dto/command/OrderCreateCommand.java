package kr.hhplus.be.server.domain.order.dto.command;

import java.util.List;

public record OrderCreateCommand(
        Long userId,
        List<OrderItemCommand> products,
        Long couponIssueId
        ) {

    public record OrderItemCommand(
            Long productId,
            int quantity
    ) {
    }

}

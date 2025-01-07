package kr.hhplus.be.server.interfaces.order.dto.request;

import java.util.List;

public record OrderCreateRequest(
        Long userId,
        List<OrderProductRequest> products,
        Long couponIssueId
) {
    public record OrderProductRequest (
            Long productId,
            int quantity
    ){}
}


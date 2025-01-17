package kr.hhplus.be.server.interfaces.order.dto.request;

import kr.hhplus.be.server.application.order.dto.criteria.OrderCreateCriteria;

import java.util.List;

public record OrderCreateRequest(
        Long userId,
        List<OrderProductRequest> products,
        Long couponIssueId
) {
    public OrderCreateCriteria toCriteria() {
        return new OrderCreateCriteria(
                this.userId(),
                this.products().stream()
                        .map(orderProductRequest -> orderProductRequest.toCriteria())
                        .toList(),
                this.couponIssueId()
        );
    }

    public record OrderProductRequest (
            Long productId,
            int quantity
    ){
        public OrderCreateCriteria.OrderItemCriteria toCriteria() {
            return new OrderCreateCriteria.OrderItemCriteria(
                    this.productId(),
                    this.quantity()
            );
        }
    }
}


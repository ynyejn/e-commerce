package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.domain.user.User;

import java.util.List;

public record OrderCreateRequest(
        List<OrderProductRequest> products,
        Long couponIssueId
) {
    public OrderCriteria.Create toCriteria(User user) {
        return new OrderCriteria.Create(
                user,
                this.products().stream()
                        .map(orderProductRequest -> orderProductRequest.toCriteria())
                        .toList(),
                this.couponIssueId()
        );
    }

    public record OrderProductRequest(
            Long productId,
            int quantity
    ) {
        public OrderCriteria.Item toCriteria() {
            return new OrderCriteria.Item(
                    this.productId(),
                    this.quantity()
            );
        }
    }
}


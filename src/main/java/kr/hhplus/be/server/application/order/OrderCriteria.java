package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.user.User;

import java.util.List;

public class OrderCriteria {
    public record Order(User user, List<Item> products, Long couponIssueId) {
        public OrderCommand.Order toCommand() {
            return new OrderCommand.Order(
                    this.user(),
                    this.products().stream()
                            .map(item -> new OrderCommand.Item(item.productId(), null, item.quantity()))
                            .toList(),
                    this.couponIssueId()
            );
        }
    }

    public record Item(Long productId, int quantity) {
    }
}

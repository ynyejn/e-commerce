package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.coupon.CouponIssue;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStock;
import kr.hhplus.be.server.domain.product.ProductStockHistory;
import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.IUserRepository;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final IOrderRepository orderRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;

    @Transactional
    public OrderInfo order(User user, OrderCreateCommand command) {
        CouponIssue couponIssue = validateCouponIssue(command.couponIssueId(), user);

        List<OrderItem> orderItems = command.products().stream()
                .map(this::createOrderItem)
                .collect(Collectors.toList());

        Order order = Order.create(user, orderItems, couponIssue);
        orderRepository.save(order);

        return OrderInfo.from(order);
    }

    private OrderItem createOrderItem(OrderCreateCommand.OrderItemCommand command) {
        Product product = productRepository.findByIdWithStock(command.productId())
                .orElseThrow(() -> new ApiException(NOT_FOUND));
        if (product.getProductStock() == null) {
            throw new ApiException(NOT_FOUND);
        }
        ProductStock productStock = productRepository.findByIdWithLock(product.getProductStock().getId())
                .orElseThrow(() -> new ApiException(NOT_FOUND));

        product.allocateStock(command.quantity());
        OrderItem orderItem = OrderItem.create(product, command.quantity());
        productRepository.save(ProductStockHistory.create(productStock, orderItem, command.quantity()));
        return orderItem;
    }

    private CouponIssue validateCouponIssue(Long couponIssueId, User user) {
        return Optional.ofNullable(couponIssueId)
                .map(user::findCouponIssue)
                .map(coupon -> {
                    coupon.validateUseable();
                    return coupon;
                })
                .orElse(null);
    }
}

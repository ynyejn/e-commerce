package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.coupon.entity.CouponIssue;
import kr.hhplus.be.server.domain.order.dto.command.OrderCreateCommand;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.entity.OrderItem;
import kr.hhplus.be.server.domain.order.repository.IOrderRepository;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.entity.ProductStockHistory;
import kr.hhplus.be.server.domain.product.repository.IProductRepository;
import kr.hhplus.be.server.domain.product.repository.IProductStockHistoryRepository;
import kr.hhplus.be.server.domain.product.repository.IProductStockRepository;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.IUserRepository;
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
    private final IProductStockRepository productStockRepository;
    private final IProductStockHistoryRepository productStockHistoryRepository;

    @Transactional
    public void order(OrderCreateCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new ApiException(NOT_FOUND));

        CouponIssue couponIssue = validateCouponIssue(command.couponIssueId(), user);

        List<OrderItem> orderItems = command.products().stream()
                .map(this::createOrderItem)
                .collect(Collectors.toList());

        Order order = Order.create(user, orderItems, couponIssue);
        orderRepository.save(order);
    }

    private OrderItem createOrderItem(OrderCreateCommand.OrderItemCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new ApiException(NOT_FOUND));
        ProductStock productStock = productStockRepository.findByIdWithLock(product.getProductStock().getId())
                .orElseThrow(() -> new ApiException(NOT_FOUND));

        product.allocateStock(command.quantity());
        OrderItem orderItem = OrderItem.create(product, command.quantity());
        productStockHistoryRepository.save(ProductStockHistory.create(productStock, orderItem, command.quantity()));
        return orderItem;
    }

    private CouponIssue validateCouponIssue(Long couponIssueId, User user) {
        return Optional.ofNullable(couponIssueId)
                .map(user::findCouponIssue)
                .map(coupon -> {
                    coupon.validate();
                    return coupon;
                })
                .orElse(null);
    }
}

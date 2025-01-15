package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.application.order.OrderConfirmCommand;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final IOrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public OrderInfo order(User user, OrderCreateCommand command) {
        Order order = Order.create(user);
        List<OrderItem> orderItems = command.products().stream()
                .map(item -> OrderItem.create(item.product(), item.quantity()))
                .collect(Collectors.toList());

        orderItems.forEach(order::addOrderItem);

        order.calculateOrderAmounts();
        orderRepository.save(order);

        return OrderInfo.from(order);
    }

    @Transactional
    public OrderInfo confirm(OrderConfirmCommand command) {
        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new ApiException(NOT_FOUND));
        order.confirm();
        eventPublisher.publishEvent(OrderEvent.from(order));
        return OrderInfo.from(order);
    }

    @Transactional
    public OrderInfo applyCoupon(Long orderId, Long couponIssueId, BigDecimal discountAmount) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(NOT_FOUND));
        order.applyCoupon(couponIssueId,discountAmount);
        return OrderInfo.from(order);
    }
}

package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final IOrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public OrderInfo order(OrderCommand.Order command) {
        Order order = Order.create(command.user());
        command.products().stream()
                .map(item -> OrderItem.create(item.product(), item.quantity()))
                .forEach(order::addOrderItem);

        order.calculateOrderAmounts();
        orderRepository.save(order);

        return OrderInfo.from(order);
    }

    @Transactional
    public OrderInfo confirm(OrderCommand.Confirm command) {
        Order order = orderRepository.findById(command.orderId()).orElseThrow(() -> new ApiException(NOT_FOUND));
        order.confirm();
        eventPublisher.publishEvent(OrderEvent.from(order));
        return OrderInfo.from(order);
    }

    @Transactional
    public OrderInfo applyCoupon(OrderCommand.ApplyCoupon command) {
        Order order = orderRepository.findById(command.orderId()).orElseThrow(() -> new ApiException(NOT_FOUND));
        order.applyCoupon(command.couponIssueId(), command.discountAmount());
        return OrderInfo.from(order);
    }
}

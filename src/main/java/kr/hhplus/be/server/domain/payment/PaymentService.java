package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.domain.order.IOrderRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final IPaymentRepository paymentRepository;
    private final IOrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional
    public OrderInfo pay(PaymentCreateCommand command) {
        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new ApiException(NOT_FOUND));

        User user = order.getUser();
        user.pay(order.getPaymentAmount()); // 사용자 포인트 검증 및 차감

        Payment payment = order.pay();  // 주문 결제 검증 및 결제 정보 생성
        paymentRepository.save(payment);
        eventPublisher.publishEvent(OrderEvent.from(order)); // 결제 이벤트 발행

        return OrderInfo.from(order);
    }
}

package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.dto.command.PaymentCreateCommand;
import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.entity.Payment;
import kr.hhplus.be.server.domain.order.event.PaymentEvent;
import kr.hhplus.be.server.domain.order.repository.IOrderRepository;
import kr.hhplus.be.server.domain.order.repository.IPaymentRepository;
import kr.hhplus.be.server.domain.user.entity.User;
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
        eventPublisher.publishEvent(PaymentEvent.from(payment)); // 결제 이벤트 발행

        return OrderInfo.from(order);
    }
}

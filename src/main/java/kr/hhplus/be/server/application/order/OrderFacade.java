package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.payment.PaymentCreateCommand;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderResult order(OrderCreateCriteria criteria) {
        OrderInfo info = orderService.order(criteria.toOrderCommand());
        info = paymentService.pay(PaymentCreateCommand.from(info.orderId()));
        return OrderResult.from(info);
    }
}

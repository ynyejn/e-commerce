package kr.hhplus.be.server.application.order.facade;

import kr.hhplus.be.server.application.order.dto.criteria.OrderCreateCriteria;
import kr.hhplus.be.server.application.order.dto.result.OrderResult;
import kr.hhplus.be.server.domain.order.dto.command.PaymentCreateCommand;
import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.domain.order.service.PaymentService;
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

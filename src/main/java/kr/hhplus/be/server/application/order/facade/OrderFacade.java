package kr.hhplus.be.server.application.order.facade;

import kr.hhplus.be.server.application.order.dto.criteria.OrderCreateCriteria;
import kr.hhplus.be.server.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;
//    private final PaymentService paymentService;

    public void order(OrderCreateCriteria criteria) {
        orderService.order(criteria.toCommand());
//        paymentService.pay();
    }
}

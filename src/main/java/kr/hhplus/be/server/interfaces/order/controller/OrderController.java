package kr.hhplus.be.server.interfaces.order.controller;

import kr.hhplus.be.server.application.order.facade.OrderFacade;
import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.interfaces.order.controller.docs.OrderControllerDocs;
import kr.hhplus.be.server.interfaces.order.dto.request.OrderCreateRequest;
import kr.hhplus.be.server.interfaces.order.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController implements OrderControllerDocs {
    private final OrderFacade orderFacade;

    /**
     * 주문/결제 API
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderCreateRequest request) {
        OrderInfo info = orderFacade.order(request.toCriteria());
        return ResponseEntity.ok(OrderResponse.from(info));
    }


}

package kr.hhplus.be.server.interfaces.order.controller;

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

    /**
     * 주문/결제 API
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderCreateRequest request) {
        OrderResponse response = new OrderResponse(
                1L,
                "2025010514001345332",
                "결제완료",
                BigDecimal.valueOf(10000),
                10,
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }


}

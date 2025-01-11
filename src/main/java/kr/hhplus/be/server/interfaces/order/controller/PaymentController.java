package kr.hhplus.be.server.interfaces.order.controller;

import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.domain.order.service.PaymentService;
import kr.hhplus.be.server.interfaces.order.controller.docs.PaymentControllerDocs;
import kr.hhplus.be.server.interfaces.order.dto.request.CreatePaymentRequest;
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
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController implements PaymentControllerDocs {
    private final PaymentService paymentService;

    /**
     * 결제 API
     * 일반적으로 주문 시 결제 한번에 되지만 재시도 등의 이유로 결제만 따로 뺀 경우 사용
     */
    @PostMapping()
    public ResponseEntity<OrderResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        OrderInfo orderInfo = paymentService.pay(request.toCommand());
        return ResponseEntity.ok(OrderResponse.from(orderInfo));
    }
}

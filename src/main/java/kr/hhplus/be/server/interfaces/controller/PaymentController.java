package kr.hhplus.be.server.interfaces.controller;

import kr.hhplus.be.server.interfaces.controller.docs.PaymentControllerDocs;
import kr.hhplus.be.server.interfaces.dto.request.CreatePaymentRequest;
import kr.hhplus.be.server.interfaces.dto.response.OrderResponse;
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

    /**
     * 결제 API
     * 일반적으로 주문 시 결제 한번에 되지만 재시도 등의 이유로 결제만 따로 뺀 경우 사용
     */
    @PostMapping()
    public ResponseEntity<OrderResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        OrderResponse response = new OrderResponse(
                request.orderId(),
                "2025010514001345332",
                "결제완료",
                BigDecimal.valueOf(10000),
                10,
                LocalDateTime.now());

        return ResponseEntity.ok(response);
    }
}

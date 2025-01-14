package kr.hhplus.be.server.interfaces.payment;

import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.interfaces.order.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

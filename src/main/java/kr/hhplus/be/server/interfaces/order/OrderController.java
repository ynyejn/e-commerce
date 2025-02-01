package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.auth.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController implements OrderControllerDocs {
    private final OrderFacade orderFacade;

    /**
     * 주문/결제 API
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@AuthenticatedUser User user,
                                                     @RequestBody OrderCreateRequest request) {
        OrderResult result = orderFacade.order(request.toCriteria(user));
        return ResponseEntity.ok(OrderResponse.from(result));
    }


}

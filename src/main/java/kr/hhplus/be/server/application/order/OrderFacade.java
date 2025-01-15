package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.CouponDiscountInfo;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.PaymentCreateCommand;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.ValidatedProductInfo;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFacade {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final ProductService productService;
    private final PointService pointService;
    private final CouponService couponService;

    @Transactional
    public OrderResult order(User user, OrderCreateCriteria criteria) {
        // 상품 검증
        List<ValidatedProductInfo> validateProducts = productService.validateProducts(criteria.toOrderItemCommands());
        // 주문 생성
        OrderInfo orderInfo = orderService.order(user, criteria.toOrderCommand(validateProducts));
        // 쿠폰 사용
        CouponDiscountInfo discountInfo = couponService.use(user, criteria.couponIssueId(), orderInfo.totalAmount());
        // 쿠폰 적용(할인 정보를 주문에도 저장하기 위함)
        orderInfo = orderService.applyCoupon(orderInfo.orderId(), discountInfo.couponIssueId(),discountInfo.discountAmount());
        // 결제
        paymentService.pay(user, PaymentCreateCommand.from(orderInfo.orderId(), orderInfo.paymentAmount()));
        // 포인트 차감
        pointService.use(user, orderInfo.paymentAmount());
        // 재고 차감
        productService.deductStock(criteria.toOrderItemCommands());
        // 주문 확정
        orderInfo = orderService.confirm(OrderConfirmCommand.from(orderInfo.orderId()));

        return OrderResult.from(orderInfo);
    }
}

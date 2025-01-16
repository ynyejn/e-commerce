package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.CouponDiscountInfo;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.order.OrderConfirmCommand;
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

        // 쿠폰 사용 및 할인 적용
        CouponDiscountInfo discountInfo = couponService.use(user, criteria.couponIssueId(), orderInfo.totalAmount());
        orderInfo = orderService.applyCoupon(orderInfo.orderId(), discountInfo.couponIssueId(), discountInfo.discountAmount());

        // 결제, 포인트 차감, 재고 차감
        paymentService.pay(user, PaymentCreateCommand.from(orderInfo.orderId(), orderInfo.paymentAmount()));
        pointService.use(user, orderInfo.paymentAmount());
        productService.deductStock(criteria.toOrderItemCommands());

        // 주문 확정
        orderInfo = orderService.confirm(OrderConfirmCommand.from(orderInfo.orderId()));

        return OrderResult.from(orderInfo);
    }
}

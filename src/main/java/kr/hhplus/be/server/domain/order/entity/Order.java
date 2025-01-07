package kr.hhplus.be.server.domain.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.constant.OrderStatus;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.support.entity.BaseEntity;
import kr.hhplus.be.server.domain.coupon.entity.CouponIssue;
import kr.hhplus.be.server.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static kr.hhplus.be.server.domain.constant.OrderStatus.PENDING;

@Entity
@Getter
@Table(name = "order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    private static final BigDecimal SHIPPING_AMOUNT = BigDecimal.valueOf(3000);
    private static final BigDecimal FREE_SHIPPING_AMOUNT = BigDecimal.valueOf(30000);
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "coupon_issue_id")
    private CouponIssue couponIssue;

    @Column(name = "order_no", nullable = false, unique = true, length = 18)
    private String orderNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "item_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal itemAmount;

    @Column(name = "shipping_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal shippingAmount;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "discount_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "payment_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    @OneToMany(mappedBy = "order", fetch = LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();


    private Order(User user, List<OrderItem> orderItems, CouponIssue couponIssue) {
        this.user = user;
        this.orderNo = createOrderNo();
        this.status = PENDING;
        this.orderItems = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            addOrderItem(orderItem);
        }
        this.discountAmount = calculateDiscountAmount(couponIssue);
        this.shippingAmount = calculateShippingAmount();
        this.totalAmount = this.itemAmount.add(this.shippingAmount);
        this.paymentAmount = this.totalAmount.subtract(this.discountAmount);
        this.couponIssue = couponIssue;

        if (couponIssue != null) {
            couponIssue.use(this);
        }
    }

    public static Order create(User user, List<OrderItem> orderItems) {
        return new Order(user, orderItems, null);
    }

    public static Order create(User user, List<OrderItem> orderItems, CouponIssue couponIssue) {
        return new Order(user, orderItems, couponIssue);
    }

    private static String createOrderNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
    }

    private void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        calculateItemAmounts();
    }

    private void calculateItemAmounts() {
        this.itemAmount = orderItems.stream()
                .map(item -> item.getOrderPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateShippingAmount() {
        if (itemAmount.compareTo(FREE_SHIPPING_AMOUNT) >= 0) {
            return BigDecimal.ZERO;
        } else {
            return SHIPPING_AMOUNT;
        }
    }

    private BigDecimal calculateDiscountAmount(CouponIssue couponIssue) {
        if (couponIssue == null) {
            return BigDecimal.ZERO;
        }
        return couponIssue.calculateDiscountAmount(this.itemAmount);
    }
}

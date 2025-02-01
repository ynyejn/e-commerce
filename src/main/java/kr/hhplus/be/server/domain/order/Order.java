package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.support.BaseEntity;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiException;
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
import static kr.hhplus.be.server.domain.order.Order.OrderStatus.PENDING;
import static kr.hhplus.be.server.support.exception.ApiErrorCode.INVALID_REQUEST;

@Entity
@Getter
@Table(name = "`order`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    private static final BigDecimal SHIPPING_AMOUNT = BigDecimal.valueOf(3000);
    private static final BigDecimal FREE_SHIPPING_AMOUNT = BigDecimal.valueOf(30000);
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "coupon_id")
    private Long couponId;

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


    private Order(User user) {
        this.user = user;
        this.orderNo = createOrderNo();
        this.status = PENDING;
        this.discountAmount = BigDecimal.ZERO;
        this.itemAmount = BigDecimal.ZERO;
        this.shippingAmount = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
        this.paymentAmount = BigDecimal.ZERO;
        this.couponId = null;
    }

    public static Order create(User user) {
        return new Order(user);
    }

    private static String createOrderNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", ThreadLocalRandom.current().nextInt(10000));
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void calculateOrderAmounts() {
        this.itemAmount = orderItems.stream()
                .map(item -> item.getOrderPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.shippingAmount = itemAmount.compareTo(FREE_SHIPPING_AMOUNT) >= 0 ? BigDecimal.ZERO : SHIPPING_AMOUNT;
        this.totalAmount = this.itemAmount.add(this.shippingAmount);
        this.paymentAmount = this.totalAmount.subtract(this.discountAmount);
    }

    public void applyCoupon(Long couponIssueId, BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
        this.paymentAmount = this.totalAmount.subtract(this.discountAmount);
        this.couponId = couponIssueId;
    }

    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new ApiException(INVALID_REQUEST);
        }
        this.status = OrderStatus.PAID;
    }

    public int getTotalQuantity() {
        return orderItems.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    public enum OrderStatus {
        PENDING("결제 대기"),
        PAID("결제 완료"),
        CANCELLED("주문 취소");

        private final String description;

        OrderStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

}

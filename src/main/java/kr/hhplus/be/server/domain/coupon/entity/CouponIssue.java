package kr.hhplus.be.server.domain.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.constant.CouponStatus;
import kr.hhplus.be.server.domain.constant.DiscountType;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.support.entity.BaseEntity;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static kr.hhplus.be.server.domain.constant.CouponStatus.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "coupon_issue")
@NoArgsConstructor(access = PROTECTED)
public class CouponIssue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    private CouponIssue(User user, Coupon coupon, LocalDateTime expiredAt) {
        this.user = user;
        this.coupon = coupon;
        this.expiredAt = expiredAt;
    }

    public static CouponIssue create(User user, Coupon coupon) {
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(coupon.getValidityPeriod());
        return new CouponIssue(user, coupon, expiredAt);
    }

    public void validate() {
        if (order != null) {
            throw new ApiException(ApiErrorCode.INVALID_REQUEST);
        }
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ApiException(ApiErrorCode.INVALID_REQUEST);
        }
    }

    public BigDecimal calculateDiscountAmount(BigDecimal orderAmount) {
        if (coupon.getMinimumOrderAmount() != null &&
                orderAmount.compareTo(coupon.getMinimumOrderAmount()) < 0) {
            throw new ApiException(ApiErrorCode.INVALID_REQUEST);
        }
        if (coupon.getDiscountType() == DiscountType.FIXED) {
            return coupon.getDiscountValue();
        } else {  // PERCENTAGE
            return orderAmount.multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100));
        }
    }

    public void use(Order order) {
        this.order = order;
    }

    public String getStatus() {
        if (order != null) {
            return USED.getDescription();
        }
        if (expiredAt.isBefore(LocalDateTime.now())) {
            return EXPIRED.getDescription();
        }
        return UNUSED.getDescription();
    }
}

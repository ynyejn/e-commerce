package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.Coupon.DiscountType;
import kr.hhplus.be.server.domain.support.BaseEntity;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static kr.hhplus.be.server.domain.coupon.CouponIssue.CouponStatus.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "coupon_issue",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "coupon_id"})
        })
@NoArgsConstructor(access = PROTECTED)
public class CouponIssue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "coupon_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Coupon coupon;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    public enum CouponStatus {
        UNUSED("미사용"),
        USED("사용 완료"),
        EXPIRED("기간 만료");

        private final String description;

        CouponStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }


    private CouponIssue(User user, Coupon coupon, LocalDateTime expiredAt) {
        this.user = user;
        this.coupon = coupon;
        this.expiredAt = expiredAt;
    }

    public static CouponIssue create(User user, Coupon coupon) {
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(coupon.getValidityPeriod());
        return new CouponIssue(user, coupon, expiredAt);
    }

    public void validateUseable(User user) {
        if (usedAt != null) {
            throw new ApiException(ApiErrorCode.INVALID_REQUEST);
        }
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ApiException(ApiErrorCode.INVALID_REQUEST);
        }
        if (!this.user.getId().equals(user.getId())) {
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

    public void use(User user) {
        validateUseable(user);
        this.usedAt = LocalDateTime.now();
    }

    public String getStatus() {
        if (usedAt != null) {
            return USED.getDescription();
        }
        if (expiredAt.isBefore(LocalDateTime.now())) {
            return EXPIRED.getDescription();
        }
        return UNUSED.getDescription();
    }
}

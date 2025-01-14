package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.support.BaseEntity;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static kr.hhplus.be.server.support.exception.ApiErrorCode.INSUFFICIENT_COUPON;
import static kr.hhplus.be.server.support.exception.ApiErrorCode.INVALID_REQUEST;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "coupon")
@NoArgsConstructor(access = PROTECTED)
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "minimum_order_amount", precision = 10, scale = 2)
    private BigDecimal minimumOrderAmount;

    @Column(name = "issue_start_at", nullable = false)
    private LocalDateTime issueStartAt;

    @Column(name = "issue_end_at", nullable = false)
    private LocalDateTime issueEndAt;

    @Column(name = "validity_period")
    private Integer validityPeriod;

    @Column(name = "total_issue_quantity")
    private Integer totalIssueQuantity;

    @Column(name = "issued_quantity", nullable = false)
    private Integer issuedQuantity = 0;

    public enum DiscountType {
        FIXED("정액"),
        PERCENTAGE("정률");

        private final String description;

        DiscountType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private Coupon(String name, DiscountType discountType, BigDecimal discountValue,
                   BigDecimal minimumOrderAmount, LocalDateTime issueStartAt,
                   LocalDateTime issueEndAt, int validityPeriod, Integer totalIssueQuantity) {
        this.name = name;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minimumOrderAmount = minimumOrderAmount;
        this.issueStartAt = issueStartAt;
        this.issueEndAt = issueEndAt;
        this.validityPeriod = validityPeriod;
        this.totalIssueQuantity = totalIssueQuantity;
    }

    public static Coupon create(String name, DiscountType discountType, BigDecimal discountValue,
                                BigDecimal minimumOrderAmount, LocalDateTime issueStartAt,
                                LocalDateTime issueEndAt, int validityPeriod, Integer totalIssueQuantity) {
        return new Coupon(name, discountType, discountValue,
                minimumOrderAmount, issueStartAt, issueEndAt, validityPeriod, totalIssueQuantity);
    }

    public CouponIssue issue(User user) {
        validateIssuable();
        this.issuedQuantity++;
        CouponIssue couponIssue = CouponIssue.create(user, this);
        return couponIssue;
    }

    private void validateIssuable() {
        // 발급 가능 기간인지 확인
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(issueStartAt) || now.isAfter(issueEndAt)) {
            throw new ApiException(INVALID_REQUEST);
        }

        // 발급 가능 수량인지 확인
        if (totalIssueQuantity != null && issuedQuantity >= totalIssueQuantity) {
            throw new ApiException(INSUFFICIENT_COUPON);
        }
    }


}

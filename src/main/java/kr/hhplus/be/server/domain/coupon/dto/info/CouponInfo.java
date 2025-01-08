package kr.hhplus.be.server.domain.coupon.dto.info;

import kr.hhplus.be.server.domain.coupon.entity.CouponIssue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponInfo(
        Long couponId,
        Long couponIssueId,
        String name,
        String status,
        String discountType,
        BigDecimal discountAmount,
        LocalDateTime expiredAt,
        LocalDateTime usedAt,
        LocalDateTime createdAt
) {
    public static CouponInfo from(CouponIssue couponIssue) {
        return new CouponInfo(
                couponIssue.getCoupon().getId(),
                couponIssue.getId(),
                couponIssue.getCoupon().getName(),
                couponIssue.getStatus(),
                couponIssue.getCoupon().getDiscountType().name(),
                couponIssue.getCoupon().getDiscountValue(),
                couponIssue.getExpiredAt(),
                couponIssue.getOrder() != null ? couponIssue.getOrder().getCreatedAt() : null,
                couponIssue.getCreatedAt()
        );
    }
}

package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.domain.coupon.CouponInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponIssueResponse(
        Long couponId,
        String status,
        String discountType,
        BigDecimal discountAmount,
        LocalDateTime expiredAt,
        LocalDateTime usedAt,
        LocalDateTime createdAt
) {
    public static CouponIssueResponse from(CouponInfo couponInfo) {
        return new CouponIssueResponse(
                couponInfo.couponId(),
                couponInfo.status(),
                couponInfo.discountType(),
                couponInfo.discountAmount(),
                couponInfo.expiredAt(),
                couponInfo.usedAt(),
                couponInfo.createdAt()
        );
    }
}

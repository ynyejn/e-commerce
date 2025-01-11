package kr.hhplus.be.server.interfaces.coupon.dto.response;

import kr.hhplus.be.server.domain.coupon.dto.info.CouponInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponResponse(
        Long couponId,
        String status,
        String discountType,
        BigDecimal discountAmount,
        LocalDateTime expiredAt,
        LocalDateTime usedAt,
        LocalDateTime createdAt
) {
    public static CouponResponse from(CouponInfo couponInfo) {
        return new CouponResponse(
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

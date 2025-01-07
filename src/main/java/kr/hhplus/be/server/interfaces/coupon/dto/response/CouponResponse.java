package kr.hhplus.be.server.interfaces.coupon.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponResponse(
        Long couponId,
        String couponCode,
        String status,
        String discountType,
        BigDecimal discountAmount,
        LocalDateTime expiredAt,
        LocalDateTime usedAt,
        LocalDateTime createdAt
) {
}

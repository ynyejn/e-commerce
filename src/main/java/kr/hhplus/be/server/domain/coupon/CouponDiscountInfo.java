package kr.hhplus.be.server.domain.coupon;

import java.math.BigDecimal;

public record CouponDiscountInfo(
        Long couponIssueId,
        BigDecimal discountAmount
) {
}

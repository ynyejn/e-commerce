package kr.hhplus.be.server.domain.coupon;

public record CouponIssueCommand(
        Long userId,
        Long couponId
) {
}

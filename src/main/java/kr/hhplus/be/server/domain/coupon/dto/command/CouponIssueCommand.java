package kr.hhplus.be.server.domain.coupon.dto.command;

public record CouponIssueCommand(
        Long userId,
        Long couponId
) {
}

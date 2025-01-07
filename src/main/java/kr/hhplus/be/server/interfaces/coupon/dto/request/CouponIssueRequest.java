package kr.hhplus.be.server.interfaces.coupon.dto.request;

public record CouponIssueRequest(
        Long couponId,
        Long userId
){
}

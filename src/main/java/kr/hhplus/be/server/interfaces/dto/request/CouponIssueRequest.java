package kr.hhplus.be.server.interfaces.dto.request;

public record CouponIssueRequest(
        Long couponId,
        Long userId
){
}

package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.domain.coupon.CouponIssueCommand;

public record CouponIssueRequest(
        Long couponId,
        Long userId
){
    public CouponIssueCommand toCommand() {
        return new CouponIssueCommand(this.couponId(), this.userId());
    }
}

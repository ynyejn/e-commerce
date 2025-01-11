package kr.hhplus.be.server.interfaces.coupon.dto.request;

import kr.hhplus.be.server.domain.coupon.dto.command.CouponIssueCommand;

public record CouponIssueRequest(
        Long couponId,
        Long userId
){
    public CouponIssueCommand toCommand() {
        return new CouponIssueCommand(this.couponId(), this.userId());
    }
}

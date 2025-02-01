package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;

import java.math.BigDecimal;

public class CouponCommand {
    public record Issue(User user, Long couponId) {
    }

    public record Use(User user, Long couponIssueId, BigDecimal paymentAmount) {
        public static Use of(User user, Long couponIssueId, BigDecimal paymentAmount) {
            return new Use(user, couponIssueId, paymentAmount);
        }
    }
}

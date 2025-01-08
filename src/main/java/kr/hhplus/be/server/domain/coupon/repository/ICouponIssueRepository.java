package kr.hhplus.be.server.domain.coupon.repository;

import kr.hhplus.be.server.domain.coupon.entity.CouponIssue;

public interface ICouponIssueRepository {
    CouponIssue save(CouponIssue couponIssue);
}

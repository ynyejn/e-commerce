package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface ICouponRepository {
    Optional<Coupon> findById(Long aLong);

    Optional<Coupon> findByIdWithLock(Long aLong);

    CouponIssue save(CouponIssue couponIssue);

}

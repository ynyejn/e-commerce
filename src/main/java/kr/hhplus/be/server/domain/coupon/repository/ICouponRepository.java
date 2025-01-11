package kr.hhplus.be.server.domain.coupon.repository;

import kr.hhplus.be.server.domain.coupon.entity.Coupon;

import java.util.Optional;

public interface ICouponRepository {
    Optional<Coupon> findById(Long aLong);

    Optional<Coupon> findByIdWithLock(Long aLong);
}

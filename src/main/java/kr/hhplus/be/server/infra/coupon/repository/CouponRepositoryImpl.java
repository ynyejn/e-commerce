package kr.hhplus.be.server.infra.coupon.repository;

import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.repository.ICouponRepository;
import kr.hhplus.be.server.infra.coupon.jpa.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements ICouponRepository {
    private final CouponJpaRepository couponJpaRepository;

    @Override
    public Optional<Coupon> findById(Long id) {
        return couponJpaRepository.findById(id);
    }

    @Override
    public Optional<Coupon> findByIdWithLock(Long id) {
        return couponJpaRepository.findByIdWithLock(id);
    }
}

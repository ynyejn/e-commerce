package kr.hhplus.be.server.infra.jpa;

import kr.hhplus.be.server.domain.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}

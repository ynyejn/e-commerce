package kr.hhplus.be.server.infra.coupon;

import kr.hhplus.be.server.domain.coupon.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, Long> {
}

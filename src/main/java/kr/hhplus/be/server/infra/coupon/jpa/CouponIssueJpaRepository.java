package kr.hhplus.be.server.infra.coupon.jpa;

import kr.hhplus.be.server.domain.coupon.entity.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, Long> {
}

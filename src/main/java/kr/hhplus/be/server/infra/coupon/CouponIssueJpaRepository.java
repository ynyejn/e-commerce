package kr.hhplus.be.server.infra.coupon;

import kr.hhplus.be.server.domain.coupon.CouponIssue;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, Long> {
    List<CouponIssue> findAllByUser(User user);
}

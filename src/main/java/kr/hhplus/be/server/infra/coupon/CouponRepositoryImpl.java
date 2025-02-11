package kr.hhplus.be.server.infra.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponIssue;
import kr.hhplus.be.server.domain.coupon.ICouponRepository;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements ICouponRepository {
    private final CouponJpaRepository couponJpaRepository;
    private final CouponIssueJpaRepository couponIssueJpaRepository;
    private final CouponCacheRepository couponCacheRepository;


    @Override
    public void saveAll(List<Coupon> couponList) {
        couponJpaRepository.saveAll(couponList);
    }
    @Override
    public Optional<Coupon> findById(Long id) {
        return couponJpaRepository.findById(id);
    }

    @Override
    public Optional<Coupon> findByIdWithLock(Long id) {
        return couponJpaRepository.findByIdWithLock(id);
    }

    @Override
    public List<Coupon> findIssuableCoupons(LocalDateTime now) {
        return couponJpaRepository.findIssuableCoupons(now);
    }

    @Override
    public CouponIssue save(CouponIssue couponIssue) {
        return couponIssueJpaRepository.save(couponIssue);
    }

    @Override
    public List<CouponIssue> findAllByUser(User user) {
        return couponIssueJpaRepository.findAllByUser(user);
    }

    @Override
    public Optional<CouponIssue> findByCouponIssueId(Long couponIssueId) {
        return couponIssueJpaRepository.findById(couponIssueId);
    }

    @Override
    public boolean isIssuedMember(Long couponId, Long userId) {
        return couponCacheRepository.isMember(couponId, userId);
    }

    @Override
    public boolean addRequest(Long couponId, Long userId) {
        return couponCacheRepository.addRequest(couponId, userId);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Long>> getRequestsByTimestamp(Long id, Integer issuableCount) {
        return couponCacheRepository.getRequestsByTimestamp(id, issuableCount);
    }

    @Override
    public int getRequestCount(Long id) {
        return couponCacheRepository.getRequestCount(id);
    }

    @Override
    public int getIssuedCount(Long id) {
        return couponCacheRepository.getIssuedCount(id);
    }

    @Override
    public int getFailedCount(Long id) {
        return couponCacheRepository.getFailedCount(id);
    }

    @Override
    public void removeRequests(Long id, Integer issuableCount) {
        couponCacheRepository.removeRequests(id, issuableCount);
    }

    @Override
    public void addIssuance(Long id, List<Long> successfulUserIds) {
        couponCacheRepository.addIssuance(id, successfulUserIds);
    }

    @Override
    public void addFailures(Long id, List<Long> failedUserIds) {
        couponCacheRepository.addFailures(id, failedUserIds);
    }

}

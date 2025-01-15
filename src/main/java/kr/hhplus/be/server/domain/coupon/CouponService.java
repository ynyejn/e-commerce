package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final ICouponRepository couponRepository;

    @Transactional
    public CouponInfo issueCoupon(User user, CouponIssueCommand command) {
        Coupon coupon = couponRepository.findByIdWithLock(command.couponId()).orElseThrow(() -> new ApiException(NOT_FOUND));

        CouponIssue couponIssue = coupon.issue(user);
        try {
            couponIssue = couponRepository.save(couponIssue);
        } catch (DataIntegrityViolationException ex) {
            throw new ApiException(ApiErrorCode.CONFLICT);
        }
        return CouponInfo.from(couponIssue);
    }

    @Transactional(readOnly = true)
    public List<CouponInfo> getCoupons(User user) {
        List<CouponIssue> couponIssues = couponRepository.findAllByUser(user);
        return couponIssues.stream()
                .map(CouponInfo::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CouponDiscountInfo use(User user, Long couponIssueId, BigDecimal totalAmount) {
        if (couponIssueId == null) {
            return new CouponDiscountInfo(null, BigDecimal.ZERO, totalAmount, totalAmount);
        }

        CouponIssue couponIssue = couponRepository.findByCouponIssueId(couponIssueId)
                .orElseThrow(() -> new ApiException(NOT_FOUND));
        BigDecimal discountAmount = couponIssue.calculateDiscountAmount(totalAmount);
        couponIssue.use(user);
        return new CouponDiscountInfo(couponIssue.getId(), discountAmount, totalAmount, totalAmount.subtract(discountAmount));
    }
}

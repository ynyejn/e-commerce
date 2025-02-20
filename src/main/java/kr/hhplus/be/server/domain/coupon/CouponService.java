package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.support.DistributedLock;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher couponEventPublisher;

    @Transactional
    @DistributedLock(key = "'coupon:' + #command.couponId()")
    public CouponInfo issueCoupon(CouponCommand.Issue command) {
        Coupon coupon = couponRepository.findById(command.couponId()).orElseThrow(() -> new ApiException(NOT_FOUND));

        CouponIssue couponIssue = coupon.issue(command.user());
        try {
            couponIssue = couponRepository.save(couponIssue);
        } catch (DataIntegrityViolationException ex) {
            throw new ApiException(ApiErrorCode.CONFLICT);
        }
        return CouponInfo.from(couponIssue);
    }

    @Transactional
    public boolean requestConponIssue(CouponCommand.Issue command) {
        Coupon coupon = couponRepository.findById(command.couponId()).orElseThrow(() -> new ApiException(NOT_FOUND));
        coupon.validateIssuable();

        if (couponRepository.isIssuedMember(command.couponId(), command.user().getId())) {
            throw new ApiException(ApiErrorCode.CONFLICT);
        }

        return couponRepository.addRequest(command.couponId(), command.user().getId());
    }


    @Transactional
    public boolean enqueue(CouponCommand.Issue command) {
        couponRepository.findById(command.couponId()).orElseThrow(() -> new ApiException(NOT_FOUND));
        couponEventPublisher.publishEvent(CouponEvent.Issue.of(command.couponId(), command.user().getId()));
        return true;
    }

    @Transactional(readOnly = true)
    public List<CouponInfo> getCoupons(User user) {
        List<CouponIssue> couponIssues = couponRepository.findAllByUser(user);
        return couponIssues.stream()
                .map(CouponInfo::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CouponDiscountInfo use(CouponCommand.Use command) {
        if (command.couponIssueId() == null) {
            return new CouponDiscountInfo(null, BigDecimal.ZERO);
        }

        CouponIssue couponIssue = couponRepository.findByCouponIssueId(command.couponIssueId()).orElseThrow(() -> new ApiException(NOT_FOUND));
        BigDecimal discountAmount = couponIssue.calculateDiscountAmount(command.paymentAmount());
        couponIssue.use(command.user());
        return new CouponDiscountInfo(couponIssue.getId(), discountAmount);
    }
}

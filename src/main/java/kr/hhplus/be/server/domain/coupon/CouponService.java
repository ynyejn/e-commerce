package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.IUserRepository;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final ICouponRepository couponRepository;
    private final IUserRepository userRepository;

    @Transactional
    public CouponInfo issueCoupon(CouponIssueCommand command) {
        User user = userRepository.findById(command.userId()).orElseThrow(() -> new ApiException(NOT_FOUND));
        Coupon coupon = couponRepository.findByIdWithLock(command.couponId()).orElseThrow(() -> new ApiException(NOT_FOUND));

        CouponIssue couponIssue = coupon.issue(user);
        try {
            couponIssue = couponRepository.save(couponIssue);
        } catch (DataIntegrityViolationException ex) {
            throw new ApiException(ApiErrorCode.CONFLICT);
        }
        return CouponInfo.from(couponIssue);
    }
}

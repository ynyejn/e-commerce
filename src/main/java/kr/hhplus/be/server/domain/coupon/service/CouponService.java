package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.dto.command.CouponIssueCommand;
import kr.hhplus.be.server.domain.coupon.dto.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.CouponIssue;
import kr.hhplus.be.server.domain.coupon.repository.ICouponIssueRepository;
import kr.hhplus.be.server.domain.coupon.repository.ICouponRepository;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.IUserRepository;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final ICouponRepository couponRepository;
    private final ICouponIssueRepository couponIssueRepository;
    private final IUserRepository userRepository;

    @Transactional
    public CouponInfo issueCoupon(CouponIssueCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new ApiException(NOT_FOUND));
        Coupon coupon = couponRepository.findByIdWithLock(command.couponId())
                .orElseThrow(() -> new ApiException(NOT_FOUND));

        CouponIssue couponIssue = coupon.issue(user);
        return CouponInfo.from(couponIssueRepository.save(couponIssue));
    }
}

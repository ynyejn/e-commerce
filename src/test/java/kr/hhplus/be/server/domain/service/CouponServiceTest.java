package kr.hhplus.be.server.domain.service;

import kr.hhplus.be.server.domain.coupon.*;
import kr.hhplus.be.server.domain.user.IUserRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private ICouponRepository couponRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private CouponService couponService;

    @Test
    void 쿠폰발급시_사용자가_존재하지_않으면_NOT_FOUND_예외가_발생한다() {
        // given
        CouponIssueCommand command = new CouponIssueCommand(1L, 1L);
        when(userRepository.findById(command.userId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> couponService.issueCoupon(command))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(NOT_FOUND);
    }

    @Test
    void 쿠폰발급시_쿠폰이_존재하지_않으면_NOT_FOUND_예외가_발생한다() {
        // given
        CouponIssueCommand command = new CouponIssueCommand(1L, 1L);
        User user = User.create("테스트유저");

        when(userRepository.findById(command.userId())).thenReturn(Optional.of(user));
        when(couponRepository.findByIdWithLock(command.couponId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> couponService.issueCoupon(command))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(NOT_FOUND);
    }

    @Test
    void 쿠폰발급_시_DB_제약조건_위반으로_CONFLICT_예외가_발생한다() {
        // given
        Long userId = 1L;
        Long couponId = 1L;
        CouponIssueCommand command = new CouponIssueCommand(userId, couponId);

        User user = mock(User.class);
        Coupon coupon = mock(Coupon.class);
        CouponIssue couponIssue = mock(CouponIssue.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(couponRepository.findByIdWithLock(couponId)).thenReturn(Optional.of(coupon));
        when(coupon.issue(user)).thenReturn(couponIssue);
        when(couponRepository.save(any(CouponIssue.class)))
                .thenThrow(new DataIntegrityViolationException("유니크키 에러"));

        // when & then
        assertThatThrownBy(() -> couponService.issueCoupon(command))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("apiErrorCode", ApiErrorCode.CONFLICT);

        verify(userRepository).findById(userId);
        verify(couponRepository).findByIdWithLock(couponId);
        verify(coupon).issue(user);
        verify(couponRepository).save(couponIssue);
    }

}
package kr.hhplus.be.server.domain.service;

import kr.hhplus.be.server.domain.coupon.dto.command.CouponIssueCommand;
import kr.hhplus.be.server.domain.coupon.repository.ICouponIssueRepository;
import kr.hhplus.be.server.domain.coupon.repository.ICouponRepository;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.IUserRepository;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private ICouponRepository couponRepository;

    @Mock
    private ICouponIssueRepository couponIssueRepository;

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
}
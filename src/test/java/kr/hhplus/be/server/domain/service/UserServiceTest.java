package kr.hhplus.be.server.domain.service;

import kr.hhplus.be.server.domain.user.dto.command.PointChargeCommand;
import kr.hhplus.be.server.domain.user.dto.info.PointInfo;
import kr.hhplus.be.server.domain.user.entity.Point;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.IPointRepository;
import kr.hhplus.be.server.domain.user.repository.IUserRepository;
import kr.hhplus.be.server.domain.user.service.UserService;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private IUserRepository userRepository;

    @Mock
    private IPointRepository pointRepository;

    @InjectMocks
    private UserService userService;


    @Test
    void 포인트충전시_사용자가_존재하지않으면_NOT_FOUND_예외가_발생한다() {
        // given
        PointChargeCommand command = new PointChargeCommand(1L, BigDecimal.valueOf(10000));
        when(userRepository.findById(command.userId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.chargePoint(command))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(ApiErrorCode.NOT_FOUND);
    }

    @Test
    void 포인트조회시_사용자가_존재하지않으면_NOT_FOUND_예외가_발생한다() {
        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getPoint(userId))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(ApiErrorCode.NOT_FOUND);
    }

    @Test
    void 포인트조회시_포인트정보가_없으면_NOT_FOUND_예외가_발생한다() {
        // given
        Long userId = 1L;
        User user = createUser();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> userService.getPoint(userId))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(ApiErrorCode.NOT_FOUND);
    }

    @Test
    void 쿠폰목록조회시_사용자가_존재하지않으면_NOT_FOUND_예외가_발생한다() {
        // given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getCoupons(userId))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(ApiErrorCode.NOT_FOUND);
    }

    private User createUser() {
        return User.create("테스트유저");
    }

}
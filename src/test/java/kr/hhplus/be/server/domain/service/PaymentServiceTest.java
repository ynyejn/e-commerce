package kr.hhplus.be.server.domain.service;

import kr.hhplus.be.server.domain.payment.*;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private IPaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void 결제_생성시_정상적으로_저장되고_결제정보가_반환된다() {
        // given
        User user = User.create("테스트 유저");
        Long orderId = 1L;
        BigDecimal paymentAmount = BigDecimal.valueOf(10000);
        PaymentCommand.Pay command = new PaymentCommand.Pay(user, orderId, paymentAmount);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        PaymentInfo result = paymentService.pay(command);

        // then
        assertThat(result.orderId()).isEqualTo(orderId);
        assertThat(result.paymentAmount()).isEqualTo(paymentAmount);

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void 결제_금액이_0원_이하인_경우_예외가_발생한다() {
        // given
        User user = User.create("테스트 유저");
        PaymentCommand.Pay command = new PaymentCommand.Pay(user, 1L, BigDecimal.ZERO);

        // when & then
        assertThatThrownBy(() -> paymentService.pay(command))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("apiErrorCode", ApiErrorCode.INVALID_REQUEST);

        verify(paymentRepository, never()).save(any());
    }
}
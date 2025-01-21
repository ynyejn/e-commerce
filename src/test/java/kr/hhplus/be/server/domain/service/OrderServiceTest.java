package kr.hhplus.be.server.domain.service;

import kr.hhplus.be.server.domain.order.OrderConfirmCommand;
import kr.hhplus.be.server.domain.order.*;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private OrderService orderService;

    @Test
    void 주문_생성_후_주문금액이_계산된다() {
        // given
        User user = mock(User.class);
        Product product = Product.create("테스트상품", BigDecimal.valueOf(10000));
        ReflectionTestUtils.setField(product, "id", 1L);
        OrderCreateCommand command = new OrderCreateCommand(
                List.of(new OrderCreateCommand.OrderItemCommand(1L,product, 2)),null
        );

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        OrderInfo result = orderService.order(user, command);

        // then
        assertThat(result.totalAmount()).isEqualTo(BigDecimal.valueOf(23000)); // 배송비 포함
        verify(orderRepository).save(any(Order.class));
    }


    @Test
    void 존재하지_않는_주문_확정시_NOT_FOUND_예외가_발생한다() {
        // given
        Long nonExistentOrderId = 999L;
        when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.confirm(new OrderConfirmCommand(nonExistentOrderId)))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("apiErrorCode", NOT_FOUND);

        verify(eventPublisher, never()).publishEvent(any());
    }



    @Test
    void 존재하지_않는_주문에_쿠폰_적용시_NOT_FOUND_예외가_발생한다() {
        // given
        Long nonExistentOrderId = 999L;
        when(orderRepository.findById(nonExistentOrderId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                orderService.applyCoupon(nonExistentOrderId, 1L, BigDecimal.valueOf(5000)))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("apiErrorCode", NOT_FOUND);
    }
}
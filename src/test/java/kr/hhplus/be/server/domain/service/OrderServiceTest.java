package kr.hhplus.be.server.domain.service;

import kr.hhplus.be.server.domain.order.dto.command.OrderCreateCommand;
import kr.hhplus.be.server.domain.order.repository.IOrderRepository;
import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.domain.product.repository.IProductRepository;
import kr.hhplus.be.server.domain.user.repository.IUserRepository;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private IOrderRepository orderRepository;
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IProductRepository productRepository;
    @InjectMocks
    private OrderService orderService;

    @Test
    void 주문시_사용자가_존재하지_않으면_NOT_FOUND_에러가_발생한다() {
        // given
        OrderCreateCommand command = new OrderCreateCommand(1L, List.of(new OrderCreateCommand.OrderItemCommand(1L,10)),null);
        when(userRepository.findById(command.userId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.order(command))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(ApiErrorCode.NOT_FOUND);

        verify(userRepository).findById(command.userId());
    }
    @Test
    void 주문시_상품이_존재하지_않으면_NOT_FOUND_에러가_발생한다() {
        // given
        OrderCreateCommand command = new OrderCreateCommand(1L, List.of(new OrderCreateCommand.OrderItemCommand(1L,10)),null);
        when(userRepository.findById(command.userId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.order(command))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(ApiErrorCode.NOT_FOUND);

        verify(userRepository).findById(command.userId());
    }

    //주문시 재고 없으면 나가리

}
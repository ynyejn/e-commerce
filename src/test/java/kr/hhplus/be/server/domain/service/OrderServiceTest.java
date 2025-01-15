package kr.hhplus.be.server.domain.service;

import kr.hhplus.be.server.domain.order.IOrderRepository;
import kr.hhplus.be.server.domain.order.OrderCreateCommand;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.IUserRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void 주문시_상품이_존재하지_않으면_NOT_FOUND_예외가_발생한다() {
        // given
        User user = User.create("테스트유저");
        OrderCreateCommand command = new OrderCreateCommand(1L, List.of(new OrderCreateCommand.OrderItemCommand(1L, 10)), null);

        // when & then
        assertThatThrownBy(() -> orderService.order(user, command))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(NOT_FOUND);

    }

    @Test
    void 주문시_재고정보가_존재하지_않으면_NOT_FOUND_예외가_발생한다() {
        // given
        OrderCreateCommand command = new OrderCreateCommand(1L, List.of(new OrderCreateCommand.OrderItemCommand(1L, 10)), null);
        User user = User.create("테스트유저");
        Product product = Product.create("테스트상품", BigDecimal.valueOf(10000));

        when(userRepository.findById(command.userId())).thenReturn(Optional.of(user));
        when(productRepository.findByIdWithStock(any())).thenReturn(Optional.of(product));

        // when & then
        assertThatThrownBy(() -> orderService.order(user, command))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(NOT_FOUND);

        verify(productRepository, never()).findByIdWithLock(any());
    }
}
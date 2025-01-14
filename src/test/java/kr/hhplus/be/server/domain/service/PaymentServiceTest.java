package kr.hhplus.be.server.domain.service;

import kr.hhplus.be.server.domain.order.IOrderRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.domain.payment.IPaymentRepository;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentCreateCommand;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private IPaymentRepository paymentRepository;
    @Mock
    private IOrderRepository orderRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private PaymentService paymentService;

    @Test
    void 결제시_주문_정보가_존재하지_않으면_NOT_FOUND_예외가_발생한다() {
        // given
        PaymentService paymentService = new PaymentService(paymentRepository, orderRepository, null);
        PaymentCreateCommand command = new PaymentCreateCommand(1L);

        // when & then
        when(orderRepository.findById(command.orderId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.pay(command))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(NOT_FOUND);
    }

    @Test
    void 결제시_결제가_완료되면_결제_이벤트가_발생한다() {
        // given
        PaymentCreateCommand command = new PaymentCreateCommand(1L);
        Order order = createOrder();
        User user = order.getUser();
        user.chargePoint(BigDecimal.valueOf(10000));

        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        PaymentService paymentService = new PaymentService(paymentRepository, orderRepository, eventPublisher);

        when(orderRepository.findById(command.orderId())).thenReturn(Optional.of(order));

        // when
        paymentService.pay(command);

        // then
        verify(eventPublisher).publishEvent(any(OrderEvent.class));
        verify(paymentRepository).save(any(Payment.class));
    }


    private User createUser() {
        return User.create("연예진");
    }

    private Order createOrder() {
        User user = createUser();
        List<OrderItem> orderItems = createOrderItems();
        return Order.create(user, orderItems);
    }

    private OrderItem createOrderItem(String name, int price, int quantity) {
        Product product = Product.create(name, BigDecimal.valueOf(price));
        return OrderItem.create(product, quantity);
    }

    private List<OrderItem> createOrderItems() {
        return List.of(createOrderItem("테스트상품", 100, 1));
    }

}
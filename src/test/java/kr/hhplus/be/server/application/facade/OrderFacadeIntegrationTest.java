package kr.hhplus.be.server.application.facade;

import kr.hhplus.be.server.application.order.dto.criteria.OrderCreateCriteria;
import kr.hhplus.be.server.application.order.dto.result.OrderResult;
import kr.hhplus.be.server.application.order.facade.OrderFacade;
import kr.hhplus.be.server.domain.constant.OrderStatus;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.repository.IOrderRepository;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.repository.IProductRepository;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.IUserRepository;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.INVALID_REQUEST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"})
class OrderFacadeIntegrationTest {
    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private IProductRepository productRepository;

    @Test
    void 포인트가_부족하면_주문은_저장되고_결제만_실패한다() {
        // given
        OrderCreateCriteria criteria = new OrderCreateCriteria(
                1L,
                List.of(new OrderCreateCriteria.OrderItemCriteria(1L, 15)),  // 150,000원 > 보유 포인트
                null
        );

        // when & then
        assertThatThrownBy(() -> orderFacade.order(criteria))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(INVALID_REQUEST);

        // 주문이 저장되었는지 확인
        List<Order> orders = orderRepository.findByUserId(1L);
        assertThat(orders)
                .isNotEmpty()
                .hasSize(2) // test-data.sql에 의해 1개가 이미 저장되어 있음
                .element(1)
                .satisfies(order -> {
                    assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
                    assertThat(order.getPaymentAmount()).isEqualTo(BigDecimal.valueOf(150000).setScale(2));
                });

        // 재고는 차감되었는지 확인
        Product product = productRepository.findById(1L).orElseThrow();
        assertThat(product.getProductStock().getQuantity()).isEqualTo(85);  // 100 - 15

        // 포인트는 차감되지 않았는지 확인
        User user = userRepository.findById(1L).orElseThrow();
        assertThat(user.getPoint().getPoint())
                .isEqualTo(BigDecimal.valueOf(100000).setScale(2));
    }

    @Test
    void 주문과_결제가_한번에_정상적으로_처리된다() {
        // given
        OrderCreateCriteria criteria = new OrderCreateCriteria(
                1L,  // 테스트 유저 (잔액 100,000원)
                List.of(new OrderCreateCriteria.OrderItemCriteria(2L, 2)),  // 다른 상품으로 테스트
                null  // 쿠폰 미사용
        );

        // when
        OrderResult result = orderFacade.order(criteria);

        // then
        assertThat(result)
                .isNotNull()
                .satisfies(order -> {
                    assertThat(order.status()).isEqualTo("결제 완료");
                    assertThat(order.totalAmount()).isEqualTo(BigDecimal.valueOf(30000).setScale(2));
                    assertThat(order.paidAt()).isNotNull();
                });

        // 재고 확인 (상품2는 초기 재고 50개)
        Product product = productRepository.findById(2L).orElseThrow();
        assertThat(product.getProductStock().getQuantity()).isEqualTo(48);  // 50 - 2

        // 포인트 확인
        User user = userRepository.findById(1L).orElseThrow();
        assertThat(user.getPoint().getPoint())
                .isEqualTo(BigDecimal.valueOf(70000).setScale(2));  // 100000 - 30000
    }
}
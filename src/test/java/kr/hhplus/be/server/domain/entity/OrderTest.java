package kr.hhplus.be.server.domain.entity;

import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.entity.OrderItem;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.user.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class OrderTest {



    @Test
    void 상품금액이_배송비_무료기준_미만이면_배송비가_추가된다() {
        // given
        User user = createUser();
        List<OrderItem> orderItems = List.of(
                createOrderItem("상품1", 10000, 1)
        );

        // when
        Order order = Order.create(user, orderItems);

        // then
        assertThat(order.getShippingAmount()).isEqualTo(BigDecimal.valueOf(3000));
        assertThat(order.getTotalAmount()).isEqualTo(BigDecimal.valueOf(13000));
    }

    @Test
    void 상품금액이_배송비_무료기준_이상이면_배송비가_무료다() {
        // given
        User user = createUser();
        List<OrderItem> orderItems = List.of(
                createOrderItem("상품1", 30000, 1)
        );

        // when
        Order order = Order.create(user, orderItems);

        // then
        assertThat(order.getShippingAmount()).isEqualTo(BigDecimal.ZERO);
        assertThat(order.getTotalAmount()).isEqualTo(BigDecimal.valueOf(30000));
    }

    @Test
    void 여러개의_주문상품의_총_금액이_정확히_계산된다() {
        // given
        User user = createUser();
        List<OrderItem> orderItems = List.of(
                createOrderItem("상품1", 10000, 2),
                createOrderItem("상품2", 20000, 1)
        );

        // when
        Order order = Order.create(user, orderItems);

        // then
        assertThat(order.getItemAmount()).isEqualTo(BigDecimal.valueOf(40000));
        assertThat(order.getShippingAmount()).isEqualTo(BigDecimal.ZERO);
        assertThat(order.getTotalAmount()).isEqualTo(BigDecimal.valueOf(40000));
    }

    private User createUser() {
        return User.create("연예진");
    }

    private OrderItem createOrderItem(String name, int price, int quantity) {
        Product product = Product.create(name, BigDecimal.valueOf(price));
        return OrderItem.create(product, quantity);
    }

    private List<OrderItem> createOrderItems() {
        return List.of(createOrderItem("테스트상품", 10000, 1));
    }
}
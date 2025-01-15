package kr.hhplus.be.server.domain.service.integration;

import kr.hhplus.be.server.domain.order.OrderCreateCommand;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"})
class OrderServiceIntegrationTest {
    @Autowired
    private OrderService orderService;

    @Test
    void 주문생성시_재고가_정상적으로_차감되고_주문정보가_생성된다() {
        // given
        User user = User.create("테스트유저");
        OrderCreateCommand command = new OrderCreateCommand(
                1L,  // 테스트 유저
                List.of(new OrderCreateCommand.OrderItemCommand(1L, 10)),  // 테스트상품1 10개
                null  // 쿠폰 미사용
        );

        // when
        OrderInfo orderInfo = orderService.order(user, command);

        // then
        assertThat(orderInfo)
                .isNotNull()
                .satisfies(info -> {
                    assertThat(info.status()).isEqualTo("결제 대기");
                    assertThat(info.totalAmount()).isEqualTo(BigDecimal.valueOf(100000).setScale(2));  // 10개 * 10000원
                });
    }

    @Test
    void 주문생성시_쿠폰할인이_정상적으로_적용된다() {
        // given
        User user = User.create("테스트유저");
        OrderCreateCommand command = new OrderCreateCommand(
                2L,  // 테스트 유저
                List.of(new OrderCreateCommand.OrderItemCommand(1L, 5)),  // 테스트상품1 5개
                3L   // 정액할인 쿠폰 (5000원)
        );

        // when
        OrderInfo orderInfo = orderService.order(user, command);

        // then
        assertThat(orderInfo)
                .isNotNull()
                .satisfies(info -> {
                    assertThat(info.totalAmount()).isEqualTo(BigDecimal.valueOf(45000).setScale(2));  // 5개 * 10000원 - 5000원
                });
    }
}

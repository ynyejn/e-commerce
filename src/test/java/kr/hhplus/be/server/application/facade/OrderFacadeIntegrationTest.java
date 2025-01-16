package kr.hhplus.be.server.application.facade;

import kr.hhplus.be.server.application.order.OrderCreateCriteria;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.domain.point.IPointRepository;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.product.ProductStock;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.IUserRepository;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

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
    private IProductRepository productRepository;

    @Autowired
    private IPointRepository pointRepository;

    @Test
    @Transactional
    void 주문생성_결제_쿠폰적용_포인트사용_재고차감이_정상적으로_처리된다() {
        // given
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));

        OrderCreateCriteria criteria = new OrderCreateCriteria(
                List.of(new OrderCreateCriteria.OrderItemCriteria(1L, 1)),  // 테스트상품1 1개
                4L  // 10% 할인 쿠폰
        );

        ProductStock beforeStock = productRepository.findByProductId(1L)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));
        int initialStock = beforeStock.getQuantity();
        Point point = pointRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("포인트 정보가 없습니다."));
        BigDecimal initialPoint = point.getPoint();

        // when
        OrderResult result = orderFacade.order(user, criteria);

        // then
        // 주문 결과
        assertThat(result).isNotNull();
        assertThat(result.status()).isEqualTo("결제 완료");
        assertThat(result.totalAmount()).isEqualTo(BigDecimal.valueOf(13000).setScale(2));  // 상품가격+배송비
        assertThat(result.paymentAmount()).isEqualTo(BigDecimal.valueOf(11700).setScale(2));  // 10% 할인 적용

        // 재고 차감
        ProductStock afterStock = productRepository.findByProductId(1L)
                .orElseThrow(() -> new RuntimeException("재고 정보가 없습니다."));
        assertThat(afterStock.getQuantity()).isEqualTo(initialStock - 1);

        // 포인트 차감
        assertThat(point.getPoint().setScale(2))
                .isEqualTo(initialPoint.subtract(result.paymentAmount()));
    }

    @Test
    void 잔액이_부족한_경우_INVALID_REQUEST_예외가_발생하여_주문이_실패하고_재고가_차감되지_않는다() {
        // given
        User user = userRepository.findById(2L)  // 포인트가 없는 사용자
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));

        OrderCreateCriteria criteria = new OrderCreateCriteria(
                List.of(new OrderCreateCriteria.OrderItemCriteria(1L, 1)),
                null  // 쿠폰 미사용
        );

        ProductStock beforeStock = productRepository.findByProductId(1L)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));
        int initialStock = beforeStock.getQuantity();

        // when & then
        assertThatThrownBy(() -> orderFacade.order(user, criteria))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("apiErrorCode", INVALID_REQUEST);

        ProductStock afterStock = productRepository.findByProductId(1L)
                .orElseThrow(() -> new RuntimeException("재고 정보가 없습니다."));
        assertThat(afterStock.getQuantity()).isEqualTo(initialStock);
    }

    @Test
    void 재고가_부족한_경우_INSUFFICIENT_STOCK_예외가_발생하여_주문이_실패하고_포인트가_차감되지_않는다() {
        // given
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));

        Point point = pointRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("포인트 정보가 없습니다."));
        BigDecimal initialPoint = point.getPoint();

        OrderCreateCriteria criteria = new OrderCreateCriteria(
                List.of(new OrderCreateCriteria.OrderItemCriteria(3L, 999)),  // 재고보다 많은 수량
                null
        );

        // when & then
        assertThatThrownBy(() -> orderFacade.order(user, criteria))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("apiErrorCode", ApiErrorCode.INSUFFICIENT_STOCK);

        assertThat(point.getPoint()).isEqualTo(initialPoint);  // 트랜잭션 롤백으로 초기값 유지
    }
}
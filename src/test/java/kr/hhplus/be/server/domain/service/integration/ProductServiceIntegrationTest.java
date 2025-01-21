package kr.hhplus.be.server.domain.service.integration;

import kr.hhplus.be.server.domain.order.OrderCreateCommand;
import kr.hhplus.be.server.domain.product.*;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"})
class ProductServiceIntegrationTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private IProductRepository productRepository;

    @Test
    void 상품_목록_조회시_페이징_처리되어_반환된다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<ProductInfo> productInfos = productService.getAllProducts(pageable);

        // then
        assertThat(productInfos).isNotNull();
        assertThat(productInfos.getContent())
                .isNotEmpty()
                .hasSize(3);  // test-data.sql의 상품 수
    }

    @Test
    void 인기상품_조회시_판매량_순으로_5개가_반환된다() {
        // when
        List<PopularProductInfo> popularProducts = productService.getTopFivePopularProducts();

        // then
        assertThat(popularProducts)
                .isNotEmpty()
                .hasSizeLessThanOrEqualTo(5)
                .isSortedAccordingTo(Comparator.comparing(PopularProductInfo::rank));
    }

    @Test
    void 상품_검증시_상품정보가_정상적으로_반환된다() {
        // given
        Long productId = 1L;
        List<OrderCreateCommand.OrderItemCommand> commands = List.of(
                new OrderCreateCommand.OrderItemCommand(productId, null, 5)
        );

        // when
        List<ValidatedProductInfo> validatedProducts = productService.validateProducts(commands);

        // then
        assertThat(validatedProducts)
                .hasSize(1)
                .allSatisfy(product -> {
                    assertThat(product.product().getId()).isEqualTo(productId);
                    assertThat(product.quantity()).isEqualTo(5);
                });
    }

    @Test
    void 존재하지_않는_상품_검증시_NOT_FOUND_예외가_발생한다() {
        // given
        Long nonExistentProductId = 999L;
        List<OrderCreateCommand.OrderItemCommand> commands = List.of(
                new OrderCreateCommand.OrderItemCommand(nonExistentProductId, null, 5)
        );

        // when & then
        assertThatThrownBy(() -> productService.validateProducts(commands))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("apiErrorCode", NOT_FOUND);
    }

    @Test
    @Transactional
    void 재고_차감시_재고수량이_정상적으로_차감된다() {
        // given
        Long productId = 1L;
        int deductQuantity = 5;
        ProductStock beforeStock = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));
        int initialStock = beforeStock.getQuantity();

        List<OrderCreateCommand.OrderItemCommand> commands = List.of(
                new OrderCreateCommand.OrderItemCommand(productId, null, deductQuantity)
        );

        // when
        productService.deductStock(commands);

        // then
        ProductStock afterStock = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("재고 정보가 없습니다."));

        assertThat(afterStock.getQuantity())
                .isEqualTo(initialStock - deductQuantity);
    }
    @Test
    void 동시에_재고_차감시_정합성이_유지된다() throws InterruptedException {
        // given
        int threadCount = 5;
        Long productId = 1L;
        int deductQuantity = 2;

        ProductStock beforeStock = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));
        int initialStock = beforeStock.getQuantity();

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    // 각 스레드에서 별도의 트랜잭션으로 실행
                    productService.deductStock(List.of(
                            new OrderCreateCommand.OrderItemCommand(productId, null, deductQuantity)
                    ));
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        // then
        latch.await(5, TimeUnit.SECONDS);
        assertThat(successCount.get()).isEqualTo(threadCount);

        Thread.sleep(100);  // 모든 스레드 종료 대기

        ProductStock afterStock = productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("재고 정보가 없습니다."));

        assertThat(afterStock.getQuantity())
                .isEqualTo(initialStock - (deductQuantity * threadCount));
    }
}
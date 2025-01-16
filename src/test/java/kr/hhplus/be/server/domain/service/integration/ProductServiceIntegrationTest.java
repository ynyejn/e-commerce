package kr.hhplus.be.server.domain.service.integration;

import kr.hhplus.be.server.domain.product.PopularProductInfo;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"})
class ProductServiceIntegrationTest {
    @Autowired
    private ProductService productService;
    @Test
    void 상품목록_조회가_정상적으로_동작한다() {
        // when
        Page<ProductInfo> products = productService.getAllProducts(PageRequest.of(0, 10));

        // then
        assertThat(products.getContent())
                .hasSize(2)
                .extracting("name", "price")
                .containsExactly(
                        tuple("테스트상품1", BigDecimal.valueOf(10000).setScale(2)),
                        tuple("테스트상품2", BigDecimal.valueOf(15000).setScale(2))
                );
    }


    @Test
    void 인기상품_조회시_판매량_순으로_정렬되어_조회된다() {
        // given - test-data.sql의 주문 데이터 기준

        // when
        List<PopularProductInfo> products = productService.getTopFivePopularProducts();

        // then
        assertThat(products)
                .hasSize(2)
                .extracting("productId", "name", "totalQuantity")
                .containsExactly(
                        tuple(1L, "테스트상품1", 2),
                        tuple(2L, "테스트상품2", 1)
                );
    }
}
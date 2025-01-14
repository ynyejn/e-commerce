package kr.hhplus.be.server.domain.entity;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStock;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ProductStockTest {

    @Test
    void ProductStock_생성시_초기수량은_0이다() {
        // given
        Product product = createProduct();

        // when
        ProductStock productStock = ProductStock.create(product);

        // then
        assertThat(productStock.getQuantity()).isZero();
    }

    @Test
    void 재고수량보다_많은_수량을_차감시_재고부족_예외가_발생한다() {
        // given
        Product product = createProduct();
        ProductStock productStock = ProductStock.create(product);

        // when & then
        assertThatThrownBy(() -> productStock.allocateStock(1))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(ApiErrorCode.INSUFFICIENT_STOCK);
    }

    @Test
    void 재고수량이_충분하면_차감된다() {
        // given
        Product product = createProduct();
        ProductStock productStock = ProductStock.create(product);
        ReflectionTestUtils.setField(productStock, "quantity", 10);

        // when
        productStock.allocateStock(3);

        // then
        assertThat(productStock.getQuantity()).isEqualTo(7);
    }

    private Product createProduct() {
        return Product.create("테스트상품", BigDecimal.valueOf(1000));
    }
}
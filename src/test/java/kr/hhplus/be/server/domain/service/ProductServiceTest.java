package kr.hhplus.be.server.domain.service;

import kr.hhplus.be.server.domain.order.repository.IOrderRepository;
import kr.hhplus.be.server.domain.product.dto.info.PopularProductInfo;
import kr.hhplus.be.server.domain.product.dto.info.ProductInfo;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.repository.IProductRepository;
import kr.hhplus.be.server.domain.product.repository.IProductStockRepository;
import kr.hhplus.be.server.domain.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;




@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IProductStockRepository productStockRepository;

    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void 상품_목록_조회시_상품이_없으면_빈_Page를_반환한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> emptyPage;
        emptyPage = new PageImpl<>(
                List.of(),
                pageable,
                0
        );
        when(productRepository.findAllProducts(pageable)).thenReturn(emptyPage);

        // when
        Page<ProductInfo> result = productService.getAllProducts(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
        verify(productRepository).findAllProducts(pageable);
    }

    @Test
    void 인기상품이_없으면_빈_리스트가_반환된다() {
        // given
        when(orderRepository.findTopFivePopularProducts()).thenReturn(List.of());

        // when
        List<PopularProductInfo> result = productService.getTopFivePopularProducts();

        // then
        assertThat(result).isEmpty();
    }
}
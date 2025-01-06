package kr.hhplus.be.server.domain.service;

import kr.hhplus.be.server.domain.dto.info.ProductInfo;
import kr.hhplus.be.server.domain.entity.Product;
import kr.hhplus.be.server.domain.repository.IProductRepository;
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
        when(productRepository.findAll(pageable)).thenReturn(emptyPage);

        // when
        Page<ProductInfo> result = productService.getAllProducts(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
        verify(productRepository).findAll(pageable);
    }
}
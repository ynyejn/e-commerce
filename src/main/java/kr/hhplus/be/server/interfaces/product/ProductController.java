package kr.hhplus.be.server.interfaces.product;


import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.support.response.PageResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController implements ProductControllerDocs {
    private final ProductService productService;

    /**
     * 상품 목록 조회 API
     */
    @GetMapping
    public PageResultResponse<List<ProductResponse>> getAllProducts(
            Pageable pageable
    ) {
        Page<ProductResponse> productPage = productService.getAllProducts(pageable)
                .map(ProductResponse::from);
        return PageResultResponse.of(productPage);
    }


    /**
     * 인기 상품 조회 API
     */
    @GetMapping("/popular/top5")
    public ResponseEntity<List<PopularProductResponse>> getTopFivePopularProducts() {

        List<PopularProductResponse> topFiveProducts =
                productService.getTopFivePopularProducts()
                .stream().map(PopularProductResponse::from).toList();

        return ResponseEntity.ok(topFiveProducts);
    }
}

package kr.hhplus.be.server.interfaces.controller;


import kr.hhplus.be.server.interfaces.controller.docs.ProductControllerDocs;
import kr.hhplus.be.server.interfaces.dto.response.PopularProductResponse;
import kr.hhplus.be.server.interfaces.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController implements ProductControllerDocs {

    /**
     * 상품 조회 API
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        ProductResponse product = new ProductResponse(
                productId,
                "맥북 프로",
                BigDecimal.valueOf(2000000),
                100
        );

        return ResponseEntity.ok(product);
    }

    /**
     * 인기 상품 조회 API
     */
    @GetMapping("/popular/top5")
    public ResponseEntity<List<PopularProductResponse>> getTopFivePopularProducts() {

        List<PopularProductResponse> topFiveProducts = List.of(
                new PopularProductResponse(1L, "맥북 프로", BigDecimal.valueOf(2000000), 150),
                new PopularProductResponse(2L, "아이패드", BigDecimal.valueOf(1000000), 120),
                new PopularProductResponse(3L, "애플워치", BigDecimal.valueOf(600000), 100),
                new PopularProductResponse(4L, "에어팟", BigDecimal.valueOf(300000), 80),
                new PopularProductResponse(5L, "아이폰", BigDecimal.valueOf(1500000), 70)
        );

        return ResponseEntity.ok(topFiveProducts);
    }
}

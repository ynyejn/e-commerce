package kr.hhplus.be.server.interfaces.product.dto.response;

import java.math.BigDecimal;

public record PopularProductResponse(
        Long rank,
        Long productId,
        String name,
        BigDecimal price,
        int quantity
) {
}


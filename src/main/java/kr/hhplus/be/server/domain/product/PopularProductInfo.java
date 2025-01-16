package kr.hhplus.be.server.domain.product;

import java.math.BigDecimal;

public record PopularProductInfo(
        Long rank,
        Long productId,
        String name,
        BigDecimal price,
        int totalQuantity
) {
}

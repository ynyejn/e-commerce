package kr.hhplus.be.server.domain.product.dto.info;

import java.math.BigDecimal;

public record PopularProductInfo(
        Long rank,
        Long productId,
        String name,
        BigDecimal price,
        int totalQuantity
) {
}

package kr.hhplus.be.server.interfaces.dto.response;

import java.math.BigDecimal;

public record PopularProductResponse(
        Long productId,
        String name,
        BigDecimal price,
        int stock
) {
}


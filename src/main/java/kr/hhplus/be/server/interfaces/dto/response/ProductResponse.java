package kr.hhplus.be.server.interfaces.dto.response;

import java.math.BigDecimal;

public record ProductResponse(
        Long productId,
        String name,
        BigDecimal price,
        int stock
) {
}

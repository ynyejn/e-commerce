package kr.hhplus.be.server.domain.product;

import java.math.BigDecimal;

public record PopularProductQuery(
        Long productId,
        String name,
        BigDecimal price,
        int totalQuantity
) {

    public PopularProductInfo toInfo(long rank) {
        return new PopularProductInfo(rank, productId, name, price, totalQuantity);
    }
}
package kr.hhplus.be.server.domain.product.dto.query;

import kr.hhplus.be.server.domain.product.dto.info.PopularProductInfo;

import java.math.BigDecimal;

public record PopularProductQuery(
    Long productId,
    String name,
    BigDecimal price,
    Long soldQuantity
) {

    public PopularProductInfo toInfo(long rank) {
        return new PopularProductInfo(rank, productId, name, price, soldQuantity);
    }
}
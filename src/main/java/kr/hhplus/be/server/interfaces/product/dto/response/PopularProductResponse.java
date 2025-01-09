package kr.hhplus.be.server.interfaces.product.dto.response;

import kr.hhplus.be.server.domain.product.dto.info.PopularProductInfo;

import java.math.BigDecimal;

public record PopularProductResponse(
        Long rank,
        Long productId,
        String name,
        BigDecimal price,
        int totalQuantity
) {
    public static PopularProductResponse from(PopularProductInfo popularProductInfo) {
        return new PopularProductResponse(
                popularProductInfo.rank(),
                popularProductInfo.productId(),
                popularProductInfo.name(),
                popularProductInfo.price(),
                popularProductInfo.totalQuantity()
        );
    }
}


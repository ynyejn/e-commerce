package kr.hhplus.be.server.interfaces.dto.response;

import kr.hhplus.be.server.domain.entity.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long productId,
        String name,
        BigDecimal price,
        int stock
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getProductStock().getQuantity());
    }
}

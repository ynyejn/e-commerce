package kr.hhplus.be.server.domain.product;

import java.math.BigDecimal;

public record ProductInfo(
        Long productId,
        String name,
        BigDecimal price,
        int stock
) {
    public static ProductInfo of(Product product, ProductStock stock) {
        return new ProductInfo(product.getId(), product.getName(), product.getPrice(), stock.getQuantity());
    }


}

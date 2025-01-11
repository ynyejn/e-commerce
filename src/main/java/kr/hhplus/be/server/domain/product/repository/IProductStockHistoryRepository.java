package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.domain.product.entity.ProductStockHistory;

public interface IProductStockHistoryRepository {
    ProductStockHistory save(ProductStockHistory productStockHistory);
}

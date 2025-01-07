package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.domain.product.entity.ProductStock;

import java.util.Optional;

public interface IProductStockRepository {
    Optional<ProductStock> findByIdWithLock(Long id);

    ProductStock save(ProductStock productStock);
}

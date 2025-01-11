package kr.hhplus.be.server.infra.product.repository;

import kr.hhplus.be.server.domain.product.entity.ProductStockHistory;
import kr.hhplus.be.server.domain.product.repository.IProductStockHistoryRepository;
import kr.hhplus.be.server.infra.product.jpa.ProductStockHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductStockHistoryRepositoryImpl implements IProductStockHistoryRepository {
    private final ProductStockHistoryJpaRepository productStockHistoryJpaRepository;

    @Override
    public ProductStockHistory save(ProductStockHistory productStockHistory) {
        return productStockHistoryJpaRepository.save(productStockHistory);
    }
}

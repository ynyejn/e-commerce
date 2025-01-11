package kr.hhplus.be.server.infra.product.repository;

import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.IProductStockRepository;
import kr.hhplus.be.server.infra.product.jpa.ProductStockJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductStockRepositoryImpl implements IProductStockRepository {
    private final ProductStockJpaRepository productStockJpaRepository;

    @Override
    public Optional<ProductStock> findByIdWithLock(Long id) {
        return productStockJpaRepository.findByIdWithLock(id);
    }

    @Override
    public ProductStock save(ProductStock productStock) {
        return productStockJpaRepository.save(productStock);
    }

}

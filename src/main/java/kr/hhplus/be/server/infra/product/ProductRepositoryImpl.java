package kr.hhplus.be.server.infra.product;

import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStock;
import kr.hhplus.be.server.domain.product.ProductStockHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements IProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final ProductQueryRepository productQueryRepository;
    private final ProductStockHistoryJpaRepository productStockHistoryJpaRepository;
    private final ProductStockJpaRepository productStockJpaRepository;


    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productJpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public Optional<Product> findByIdWithStock(Long id) {
        return productJpaRepository.findByIdWithStock(id);
    }

    @Override
    public Page<Product> findAllProducts(Pageable pageable) {
        return productQueryRepository.findAllProducts(pageable);
    }

    @Override
    public ProductStockHistory save(ProductStockHistory productStockHistory) {
        return productStockHistoryJpaRepository.save(productStockHistory);
    }

    @Override
    public Optional<ProductStock> findByIdWithLock(Long id) {
        return productStockJpaRepository.findByIdWithLock(id);
    }

    @Override
    public List<ProductStock> findAllByIdsWithLock(List<Long> productIds) {
        return productStockJpaRepository.findAllByIdsWithLock(productIds);
    }

    @Override
    public ProductStock save(ProductStock productStock) {
        return productStockJpaRepository.save(productStock);
    }

    @Override
    public List<ProductStock> saveAll(List<ProductStock> stocks) {
        return productStockJpaRepository.saveAll(stocks);
    }

    @Override
    public List<Product> findAllById(List<Long> productIds) {
        return productJpaRepository.findAllById(productIds);
    }

}

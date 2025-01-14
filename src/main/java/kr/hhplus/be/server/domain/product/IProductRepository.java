package kr.hhplus.be.server.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IProductRepository {
    Page<Product> findAll(Pageable pageable);

    Optional<Product> findById(Long id);

    Optional<Product> findByIdWithStock(Long aLong);

    Product save(Product product);

    Page<Product> findAllProducts(Pageable pageable);

    Optional<ProductStock> findByIdWithLock(Long id);

    ProductStock save(ProductStock productStock);

    ProductStockHistory save(ProductStockHistory productStockHistory);

}

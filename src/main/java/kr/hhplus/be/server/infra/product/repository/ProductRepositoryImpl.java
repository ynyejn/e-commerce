package kr.hhplus.be.server.infra.product.repository;

import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.repository.IProductRepository;
import kr.hhplus.be.server.infra.product.jpa.ProductJpaRepository;
import kr.hhplus.be.server.infra.product.query.ProductQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements IProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final ProductQueryRepository productQueryRepository;

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
}

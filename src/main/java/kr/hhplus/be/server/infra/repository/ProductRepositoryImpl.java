package kr.hhplus.be.server.infra.repository;

import kr.hhplus.be.server.domain.entity.Product;
import kr.hhplus.be.server.domain.repository.IProductRepository;
import kr.hhplus.be.server.infra.jpa.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements IProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productJpaRepository.findAll(pageable);
    }
}

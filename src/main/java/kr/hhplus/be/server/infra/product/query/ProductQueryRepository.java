package kr.hhplus.be.server.infra.product.query;

import kr.hhplus.be.server.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryRepository {
    Page<Product> findAllProducts(Pageable pageable);
}

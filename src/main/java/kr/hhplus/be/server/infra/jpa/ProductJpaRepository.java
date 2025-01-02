package kr.hhplus.be.server.infra.jpa;

import kr.hhplus.be.server.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
}

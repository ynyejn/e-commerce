package kr.hhplus.be.server.infra.product.jpa;

import kr.hhplus.be.server.domain.product.entity.ProductStockHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockHistoryJpaRepository extends JpaRepository<ProductStockHistory, Long> {

}

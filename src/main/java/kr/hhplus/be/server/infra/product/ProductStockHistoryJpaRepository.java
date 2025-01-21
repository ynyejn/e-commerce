package kr.hhplus.be.server.infra.product;

import kr.hhplus.be.server.domain.product.ProductStockHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockHistoryJpaRepository extends JpaRepository<ProductStockHistory, Long> {

}

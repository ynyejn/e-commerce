package kr.hhplus.be.server.infra.product;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.product.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductStockJpaRepository extends JpaRepository<ProductStock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ps FROM ProductStock ps WHERE ps.id = :id")
    Optional<ProductStock> findByIdWithLock(@Param("id") Long id);
}

package kr.hhplus.be.server.infra.jpa;

import kr.hhplus.be.server.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}

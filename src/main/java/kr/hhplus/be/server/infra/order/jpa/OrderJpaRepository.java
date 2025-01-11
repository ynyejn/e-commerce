package kr.hhplus.be.server.infra.order.jpa;

import kr.hhplus.be.server.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(long userId);
}

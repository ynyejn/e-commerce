package kr.hhplus.be.server.infra.outbox;

import kr.hhplus.be.server.domain.order.OrderOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderOutboxJpaRepository  extends JpaRepository<OrderOutbox, Long> {
    Optional<OrderOutbox> findByOrderId(Long orderId);
}

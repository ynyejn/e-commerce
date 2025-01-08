package kr.hhplus.be.server.infra.order.jpa;

import kr.hhplus.be.server.domain.order.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}

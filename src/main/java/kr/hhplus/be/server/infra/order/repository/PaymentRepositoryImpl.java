package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.domain.order.entity.Payment;
import kr.hhplus.be.server.domain.order.repository.IPaymentRepository;
import kr.hhplus.be.server.infra.order.jpa.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements IPaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment){
        return paymentJpaRepository.save(payment);
    }
}

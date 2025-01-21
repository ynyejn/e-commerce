package kr.hhplus.be.server.infra.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.IPaymentRepository;
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

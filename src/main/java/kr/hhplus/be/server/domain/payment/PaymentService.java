package kr.hhplus.be.server.domain.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final IPaymentRepository paymentRepository;


    @Transactional
    public PaymentInfo pay(PaymentCommand.Pay command) {
        Payment payment = Payment.create(command.orderId(), command.paymentAmount());
        payment = paymentRepository.save(payment);
        return PaymentInfo.from(payment);
    }
}

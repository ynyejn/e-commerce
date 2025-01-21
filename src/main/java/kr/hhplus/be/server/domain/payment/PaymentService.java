package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final IPaymentRepository paymentRepository;


    @Transactional
    public PaymentInfo pay(User user, PaymentCreateCommand command) {
        Payment payment = Payment.create(command.orderId(), command.paymentAmount());
        payment = paymentRepository.save(payment);
        return PaymentInfo.from(payment);
    }
}

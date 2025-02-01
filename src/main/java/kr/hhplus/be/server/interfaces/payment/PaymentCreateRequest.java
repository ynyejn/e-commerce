package kr.hhplus.be.server.interfaces.payment;

import kr.hhplus.be.server.domain.payment.PaymentCommand;
import kr.hhplus.be.server.domain.user.User;

import java.math.BigDecimal;

public record PaymentCreateRequest(
        Long orderId,
        BigDecimal paymentAmount
) {
    public PaymentCommand.Pay toCommand(User user) {
        return new PaymentCommand.Pay(user, orderId, paymentAmount);
    }
}

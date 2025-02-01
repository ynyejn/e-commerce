package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.user.User;

import java.math.BigDecimal;

public class PaymentCommand {
    public record Pay(User user, Long orderId, BigDecimal paymentAmount) {
        public static Pay of(User user, Long orderId, BigDecimal paymentAmount) {
            return new Pay(user, orderId, paymentAmount);
        }
    }
}
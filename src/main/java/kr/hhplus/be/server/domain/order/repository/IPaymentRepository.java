package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.order.entity.Payment;

public interface IPaymentRepository {
    Payment save(Payment payment);
}

package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.order.entity.Order;

public interface IOrderRepository {
    Order save(Order order);
}

package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.product.dto.query.PopularProductQuery;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository {
    Order save(Order order);

    Optional<Order> findById(Long aLong);

    List<PopularProductQuery> findTopFivePopularProducts();

    List<Order> findByUserId(long l);
}

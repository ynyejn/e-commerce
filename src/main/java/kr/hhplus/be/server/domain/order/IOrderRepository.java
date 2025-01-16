package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.PopularProductQuery;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository {
    Order save(Order order);

    Optional<Order> findById(Long aLong);

    List<PopularProductQuery> findTopFivePopularProducts();

    List<Order> findByUserId(long l);
}

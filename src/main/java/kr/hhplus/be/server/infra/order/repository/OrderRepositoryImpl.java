package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.repository.IOrderRepository;
import kr.hhplus.be.server.domain.product.dto.query.PopularProductQuery;
import kr.hhplus.be.server.infra.order.jpa.OrderJpaRepository;
import kr.hhplus.be.server.infra.order.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements IOrderRepository {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderQueryRepository orderQueryRepository;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long aLong) {
        return orderJpaRepository.findById(aLong);
    }

    @Override
    public List<PopularProductQuery> findTopFivePopularProducts() {
        return orderQueryRepository.findTopFivePopularProducts();
    }

    @Override
    public List<Order> findByUserId(long userId) {
        return orderJpaRepository.findByUserId(userId);
    }

}

package kr.hhplus.be.server.infra.order.query;


import kr.hhplus.be.server.domain.product.dto.query.PopularProductQuery;

import java.util.List;

public interface OrderQueryRepository {
    List<PopularProductQuery> findTopFivePopularProducts();
}

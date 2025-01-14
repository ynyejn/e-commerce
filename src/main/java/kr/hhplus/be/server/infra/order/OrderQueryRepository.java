package kr.hhplus.be.server.infra.order;


import kr.hhplus.be.server.domain.product.PopularProductQuery;

import java.util.List;

public interface OrderQueryRepository {
    List<PopularProductQuery> findTopFivePopularProducts();
}

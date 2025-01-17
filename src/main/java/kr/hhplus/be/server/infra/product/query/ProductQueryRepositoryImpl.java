package kr.hhplus.be.server.infra.product.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.hhplus.be.server.domain.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepositoryImpl implements ProductQueryRepository {
    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Product> findAllProducts(Pageable pageable) {
        List<Product> products = queryFactory.selectFrom(product)
                .leftJoin(product.productStock).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(product).fetchCount();

        return new PageImpl<>(products, pageable, total);
    }
}

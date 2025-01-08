package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.order.repository.IOrderRepository;
import kr.hhplus.be.server.domain.product.dto.info.PopularProductInfo;
import kr.hhplus.be.server.domain.product.dto.info.ProductInfo;
import kr.hhplus.be.server.domain.product.dto.query.PopularProductQuery;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.IProductRepository;
import kr.hhplus.be.server.domain.product.repository.IProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final IProductStockRepository productStockRepository;
    private final IOrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Page<ProductInfo> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductInfo::from);
    }

    @Transactional
    public Product createProduct(String name, BigDecimal price) {
        Product product = Product.create(name, price);
        ProductStock productStock = ProductStock.create(product);

        productRepository.save(product);
        productStockRepository.save(productStock);

        return product;
    }

    @Transactional(readOnly = true)
    public List<PopularProductInfo> getTopFivePopularProducts() {
        List<PopularProductQuery> productQueries = orderRepository.findTopFivePopularProducts();
        AtomicLong rank = new AtomicLong(1);
        return productQueries.stream()
                .map(query -> query.toInfo(rank.getAndIncrement()))
                .collect(Collectors.toList());
    }

}

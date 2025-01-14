package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.order.IOrderRepository;
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
    private final IOrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Page<ProductInfo> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAllProducts(pageable);
        return products.map(ProductInfo::from);
    }

    @Transactional
    public Product createProduct(String name, BigDecimal price) {
        Product product = Product.create(name, price);
        ProductStock productStock = ProductStock.create(product);

        productRepository.save(product);
        productRepository.save(productStock);

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

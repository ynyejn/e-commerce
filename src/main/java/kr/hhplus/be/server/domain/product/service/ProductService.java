package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.dto.info.ProductInfo;
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

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final IProductStockRepository productStockRepository;

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
}

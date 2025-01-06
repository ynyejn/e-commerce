package kr.hhplus.be.server.domain.service;

import kr.hhplus.be.server.domain.dto.info.ProductInfo;
import kr.hhplus.be.server.domain.entity.Product;
import kr.hhplus.be.server.domain.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductInfo> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductInfo::from);
    }
}

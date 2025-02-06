package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final IProductRepository productRepository;
    private final PopularProductCacheManager popularProductCacheManager;


    @Transactional(readOnly = true)
    public Page<ProductInfo> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAllProducts(pageable);
        List<Long> productIds = products.getContent().stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        List<ProductStock> productStocks = productRepository.findAllByProductIds(productIds);

        Map<Long, ProductStock> stockMap = productStocks.stream()
                .collect(Collectors.toMap(stock -> stock.getProduct().getId(), stock -> stock));

        return products.map(product -> {
            ProductStock stock = stockMap.get(product.getId());
            return ProductInfo.of(product, stock);
        });
    }

    @Transactional(readOnly = true)
    public List<PopularProductInfo> getTopFivePopularProducts() {
        return popularProductCacheManager.getTopProducts(5);
    }

    @Transactional(readOnly = true)
    public List<ValidatedProductInfo> validateProducts(List<OrderCommand.Item> orderItemCommands) {
        List<Long> productIds = orderItemCommands.stream()
                .map(OrderCommand.Item::productId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            throw new ApiException(NOT_FOUND);
        }

        return products.stream()
                .map(product -> {
                    OrderCommand.Item orderItemCommand = orderItemCommands.stream()
                            .filter(command -> command.productId() == product.getId())
                            .findFirst().orElseThrow(() -> new ApiException(NOT_FOUND));
                    return ValidatedProductInfo.of(product, orderItemCommand.quantity());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deductStock(List<OrderCommand.Item> commands) {
        List<Long> productIds = commands.stream()
                .map(OrderCommand.Item::productId)
                .collect(Collectors.toList());

        List<ProductStock> stocks = productRepository.findAllByProductIdsWithLock(productIds);

        if (stocks.size() != productIds.size()) {
            throw new ApiException(NOT_FOUND);
        }

        Map<Long, ProductStock> stockMap = stocks.stream()
                .collect(Collectors.toMap(stock -> stock.getProduct().getId(), stock -> stock));


        for (OrderCommand.Item command : commands) {
            ProductStock stock = stockMap.get(command.productId());
            stock.deduct(command.quantity());
        }

        productRepository.saveAll(stocks);
    }

}

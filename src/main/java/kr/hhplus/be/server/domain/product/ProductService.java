package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.order.IOrderRepository;
import kr.hhplus.be.server.domain.order.OrderCreateCommand;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;

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

    @Transactional(readOnly = true)
    public List<PopularProductInfo> getTopFivePopularProducts() {
        List<PopularProductQuery> productQueries = orderRepository.findTopFivePopularProducts();
        AtomicLong rank = new AtomicLong(1);
        return productQueries.stream()
                .map(query -> query.toInfo(rank.getAndIncrement()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ValidatedProductInfo> validateProducts(List<OrderCreateCommand.OrderItemCommand> orderItemCommands) {
        List<Long> productIds = orderItemCommands.stream()
                .map(OrderCreateCommand.OrderItemCommand::productId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            throw new ApiException(NOT_FOUND);
        }

        return products.stream()
                .map(product -> {
                    OrderCreateCommand.OrderItemCommand orderItemCommand = orderItemCommands.stream()
                            .filter(command -> command.productId() == product.getId())
                            .findFirst().orElseThrow(() -> new ApiException(NOT_FOUND));
                    return ValidatedProductInfo.of(product, orderItemCommand.quantity());
                })
                .collect(Collectors.toList());
    }

    public void deductStock(List<OrderCreateCommand.OrderItemCommand> commands) {
        List<Long> productIds = commands.stream()
                .map(OrderCreateCommand.OrderItemCommand::productId)
                .collect(Collectors.toList());

        // in with lock
        List<ProductStock> stocks = productRepository.findAllByIdsWithLock(productIds);

        Map<Long, ProductStock> stockMap = stocks.stream()
                .collect(Collectors.toMap(ProductStock::getId, stock -> stock));

        for (OrderCreateCommand.OrderItemCommand command : commands) {
            ProductStock stock = stockMap.get(command.productId());
            stock.deduct(command.quantity());
        }

        productRepository.saveAll(stocks);
    }
}

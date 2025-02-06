package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.order.IOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PopularProductCacheManager {
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;

    public void refreshPopularProducts() {
        try {
            List<PopularProductQuery> products = orderRepository.findTopFivePopularProducts();
            productRepository.addToTempKeys(products); // 새로운 정보 임시 키에 저장
            switchKeys();
        } catch (Exception e) {
            log.error("Failed to refresh popular products cache", e);
            productRepository.deleteKeys(
                    productRepository.getTempSortedKey(),
                    productRepository.getTempHashKey()
            );
            throw e;
        }
    }

    private void switchKeys() {
        if (productRepository.existsKey(productRepository.getCurrentSortedKey())) {
            switchWithBackup(); // 현재 키가 존재하면 백업에 저장하고 임시키를 현재키로 변경
        } else {
            productRepository.moveKeys(
                    productRepository.getTempSortedKey(),
                    productRepository.getCurrentSortedKey(),
                    productRepository.getTempHashKey(),
                    productRepository.getCurrentHashKey()
            );
        }
    }

    private void switchWithBackup() {
        productRepository.moveKeys(
                productRepository.getCurrentSortedKey(),
                productRepository.getBackupSortedKey(),
                productRepository.getCurrentHashKey(),
                productRepository.getBackupHashKey()
        );

        productRepository.moveKeys(
                productRepository.getTempSortedKey(),
                productRepository.getCurrentSortedKey(),
                productRepository.getTempHashKey(),
                productRepository.getCurrentHashKey()
        );

        productRepository.deleteKeys(
                productRepository.getBackupSortedKey(),
                productRepository.getBackupHashKey()
        );
    }

    public List<PopularProductInfo> getTopProducts(int limit) {
        Set<ZSetOperations.TypedTuple<Long>> topProducts =
                productRepository.getTopProductIds(productRepository.getCurrentSortedKey(), limit);
        String hashKey = productRepository.getCurrentHashKey();

        // 현재 키에 인기 상품이 없으면 백업키에서 가져옴
        if (topProducts == null || topProducts.isEmpty()) {
            topProducts = productRepository.getTopProductIds(productRepository.getBackupSortedKey(), limit);
            hashKey = productRepository.getBackupHashKey();
        }

        if (topProducts == null || topProducts.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> productIds = topProducts.stream()
                .map(tuple -> String.valueOf(tuple.getValue()))
                .collect(Collectors.toList());

        return productRepository.getProductHashValues(hashKey, productIds)
                .stream()
                .map(PopularProductInfo::from)
                .collect(Collectors.toList());
    }
}
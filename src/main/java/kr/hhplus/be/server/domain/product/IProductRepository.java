package kr.hhplus.be.server.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IProductRepository {
    Page<Product> findAll(Pageable pageable);

    Optional<Product> findById(Long id);

    Optional<Product> findByIdWithStock(Long aLong);

    Product save(Product product);

    Page<Product> findAllProducts(Pageable pageable);

    Optional<ProductStock> findByProductIdWithLock(Long id);

    ProductStock save(ProductStock productStock);

    ProductStockHistory save(ProductStockHistory productStockHistory);

    List<Product> findAllById(List<Long> productIds);

    List<ProductStock> findAllByProductIdsWithLock(List<Long> productIds);

    List<ProductStock> saveAll(List<ProductStock> stocks);

    Optional<ProductStock> findByProductId(Long productId);

    List<ProductStock> findAllByProductIds(List<Long> productIds);

    void addToTempKeys(List<PopularProductQuery> products);

    String getCurrentSortedKey();

    void moveKeys(String oldSortedKey, String newSortedKey, String oldHashKey, String newHashKey);

    void deleteKeys(String... keys);

    boolean existsKey(String key);

    String getTempSortedKey();

    String getBackupSortedKey();

    String getTempHashKey();

    String getCurrentHashKey();

    String getBackupHashKey();

    List<String> getProductHashValues(String hashKey, List<String> productIds);

    Set<ZSetOperations.TypedTuple<Long>> getTopProductIds(String sortedKey, int limit);


}

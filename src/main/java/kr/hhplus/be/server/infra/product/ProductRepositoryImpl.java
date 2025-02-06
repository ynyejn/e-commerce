package kr.hhplus.be.server.infra.product;

import kr.hhplus.be.server.domain.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements IProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final ProductQueryRepository productQueryRepository;
    private final ProductStockHistoryJpaRepository productStockHistoryJpaRepository;
    private final ProductStockJpaRepository productStockJpaRepository;
    private final ProductCacheRepository productCacheRepository;


    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productJpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id);
    }

    @Override
    public Optional<Product> findByIdWithStock(Long id) {
        return productJpaRepository.findByIdWithStock(id);
    }

    @Override
    public Page<Product> findAllProducts(Pageable pageable) {
        return productQueryRepository.findAllProducts(pageable);
    }

    @Override
    public ProductStockHistory save(ProductStockHistory productStockHistory) {
        return productStockHistoryJpaRepository.save(productStockHistory);
    }

    @Override
    public Optional<ProductStock> findByProductIdWithLock(Long id) {
        return productStockJpaRepository.findByProductIdWithLock(id);
    }

    @Override
    public List<ProductStock> findAllByProductIdsWithLock(List<Long> productIds) {
        return productStockJpaRepository.findAllByProductIdsWithLock(productIds);
    }

    @Override
    public ProductStock save(ProductStock productStock) {
        return productStockJpaRepository.save(productStock);
    }

    @Override
    public List<ProductStock> saveAll(List<ProductStock> stocks) {
        return productStockJpaRepository.saveAll(stocks);
    }

    @Override
    public List<Product> findAllById(List<Long> productIds) {
        return productJpaRepository.findAllById(productIds);
    }

    @Override
    public Optional<ProductStock> findByProductId(Long productId) {
        return productStockJpaRepository.findByProductId(productId);
    }

    @Override
    public List<ProductStock> findAllByProductIds(List<Long> productIds) {
        return productStockJpaRepository.findAllByProductIds(productIds);
    }

    @Override
    public void addToTempKeys(List<PopularProductQuery> products) {
        productCacheRepository.addToTempKeys(products);
    }

    @Override
    public void moveKeys(String oldSortedKey, String newSortedKey, String oldHashKey, String newHashKey) {
        productCacheRepository.moveKeys(oldSortedKey, newSortedKey, oldHashKey, newHashKey);
    }

    @Override
    public void deleteKeys(String... keys) {
        productCacheRepository.deleteKeys(keys);
    }

    @Override
    public boolean existsKey(String key) {
        return productCacheRepository.existsKey(key);
    }

    @Override
    public String getTempSortedKey() {
        return productCacheRepository.getTempSortedKey();
    }

    @Override
    public String getCurrentSortedKey() {
        return productCacheRepository.getCurrentSortedKey();
    }

    @Override
    public String getBackupSortedKey() {
        return productCacheRepository.getBackupSortedKey();
    }

    @Override
    public String getTempHashKey() {
        return productCacheRepository.getTempHashKey();
    }

    @Override
    public String getCurrentHashKey() {
        return productCacheRepository.getCurrentHashKey();
    }

    @Override
    public String getBackupHashKey() {
        return productCacheRepository.getBackupHashKey();
    }

    @Override
    public List<String> getProductHashValues(String hashKey, List<String> productIds) {
        return productCacheRepository.getProductHashValues(hashKey, productIds);
    }

    @Override
    public Set<ZSetOperations.TypedTuple<Long>> getTopProductIds(String sortedKey, int limit) {
        return productCacheRepository.getTopProductIds(sortedKey, limit);
    }


}

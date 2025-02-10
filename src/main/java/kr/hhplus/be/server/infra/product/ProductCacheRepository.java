package kr.hhplus.be.server.infra.product;

import kr.hhplus.be.server.domain.product.PopularProductQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductCacheRepository {
    private static final String TEMP_SORTED_KEY = "products:popular:temp:sorted";
    private static final String CURRENT_SORTED_KEY = "products:popular:current:sorted";
    private static final String BACKUP_SORTED_KEY = "products:popular:backup:sorted";
    private static final String TEMP_HASH_KEY = "products:popular:temp:hash";
    private static final String CURRENT_HASH_KEY = "products:popular:current:hash";
    private static final String BACKUP_HASH_KEY = "products:popular:backup:hash";
    private final RedisTemplate<String, Long> redisTemplate;

    public void addToTempKeys(List<PopularProductQuery> products) {
        if (products.isEmpty()) {
            return;
        }
        products.forEach(product -> {
            redisTemplate.opsForZSet().add(TEMP_SORTED_KEY, product.productId(), product.totalQuantity());
            redisTemplate.opsForHash().put(TEMP_HASH_KEY, String.valueOf(product.productId()), product.toProductInfoString());
        });
    }

    public void moveKeys(String oldSortedKey, String newSortedKey,
                         String oldHashKey, String newHashKey) {
        if (!existsKey(oldSortedKey) || !existsKey(oldHashKey)) {
            return;
        }
        redisTemplate.rename(oldSortedKey, newSortedKey);
        redisTemplate.rename(oldHashKey, newHashKey);
    }

    public void deleteKeys(String... keys) {
        for (String key : keys) {
            redisTemplate.delete(key);
        }
    }

    public boolean existsKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }


    public Set<ZSetOperations.TypedTuple<Long>> getTopProductIds(String sortedKey, int limit) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(sortedKey, 0, limit - 1);
    }

    public List<String> getProductHashValues(String hashKey, List<String> productIds) {
        List<Object> results = redisTemplate.opsForHash().multiGet(hashKey, new ArrayList<>(productIds));
        List<String> hashValues = new ArrayList<>();
        if (results != null) {
            results.forEach(result -> hashValues.add(result != null ? result.toString() : null));
        }
        return hashValues;
    }

    public String getTempSortedKey() {
        return TEMP_SORTED_KEY;
    }

    public String getCurrentSortedKey() {
        return CURRENT_SORTED_KEY;
    }

    public String getBackupSortedKey() {
        return BACKUP_SORTED_KEY;
    }

    public String getTempHashKey() {
        return TEMP_HASH_KEY;
    }

    public String getCurrentHashKey() {
        return CURRENT_HASH_KEY;
    }

    public String getBackupHashKey() {
        return BACKUP_HASH_KEY;
    }
}

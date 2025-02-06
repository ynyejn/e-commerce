package kr.hhplus.be.server.domain.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

public record PopularProductInfo(
        Long rank,
        Long productId,
        String name,
        BigDecimal price,
        int totalQuantity
) {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static PopularProductInfo from(String hashValue) {
        try {
            return objectMapper.readValue(hashValue, PopularProductInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse product info", e);
        }
    }
}

package kr.hhplus.be.server.interfaces.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        String orderNo,
        String status,
        BigDecimal totalAmount,
        int totalQuantity,
        LocalDateTime createdAt
) {
}

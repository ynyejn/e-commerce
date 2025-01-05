package kr.hhplus.be.server.interfaces.dto.request;

import java.util.List;

public record CreateOrderRequest(
        Long userId,
        List<OrderProductRequest> products,
        Long couponId
) {}

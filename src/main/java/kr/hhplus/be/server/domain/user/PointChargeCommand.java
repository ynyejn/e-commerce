package kr.hhplus.be.server.domain.user;

import java.math.BigDecimal;

public record PointChargeCommand(
        Long userId,
        BigDecimal amount
) {
}

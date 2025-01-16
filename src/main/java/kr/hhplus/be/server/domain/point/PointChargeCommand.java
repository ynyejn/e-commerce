package kr.hhplus.be.server.domain.point;

import java.math.BigDecimal;

public record PointChargeCommand(
        BigDecimal amount
) {
}

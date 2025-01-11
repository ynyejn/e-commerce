package kr.hhplus.be.server.domain.user.dto.command;

import java.math.BigDecimal;

public record PointChargeCommand(
        Long userId,
        BigDecimal amount
) {
}

package kr.hhplus.be.server.interfaces.user;

import kr.hhplus.be.server.domain.user.PointChargeCommand;

import java.math.BigDecimal;

public record PointChargeRequest(
        BigDecimal amount
) {
    public PointChargeCommand toCommand(Long userId) {

        return new PointChargeCommand(userId, this.amount());
    }
}

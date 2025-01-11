package kr.hhplus.be.server.interfaces.user.dto.request;

import kr.hhplus.be.server.domain.user.dto.command.PointChargeCommand;

import java.math.BigDecimal;

public record PointChargeRequest(
        BigDecimal amount
) {
    public PointChargeCommand toCommand(Long userId) {

        return new PointChargeCommand(userId, this.amount());
    }
}

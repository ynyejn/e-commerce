package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.domain.point.PointChargeCommand;

import java.math.BigDecimal;

public record PointChargeRequest(
        BigDecimal amount
) {
    public PointChargeCommand toCommand() {

        return new PointChargeCommand(this.amount());
    }
}

package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.domain.point.PointCommand;
import kr.hhplus.be.server.domain.user.User;

import java.math.BigDecimal;

public record PointChargeRequest(
        BigDecimal amount
) {
    public PointCommand.Charge toCommand(User user) {

        return new PointCommand.Charge(user, amount);
    }
}

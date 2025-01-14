package kr.hhplus.be.server.interfaces.user;

import kr.hhplus.be.server.domain.user.PointInfo;

import java.math.BigDecimal;

public record UserResponse(
        Long userId,
        String userName,
        BigDecimal point
) {
    public static UserResponse from(PointInfo response) {
        return new UserResponse(response.userId(), response.userName(), response.point());
    }
}

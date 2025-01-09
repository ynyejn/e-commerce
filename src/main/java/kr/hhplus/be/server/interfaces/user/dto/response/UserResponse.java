package kr.hhplus.be.server.interfaces.user.dto.response;

import kr.hhplus.be.server.domain.user.dto.info.PointInfo;

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

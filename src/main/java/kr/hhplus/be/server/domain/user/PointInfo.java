package kr.hhplus.be.server.domain.user;

import java.math.BigDecimal;

public record PointInfo(
        Long userId,
        String userName,
        BigDecimal point) {
    public static PointInfo from(Point point) {
        return new PointInfo(
                point.getUser().getId(),
                point.getUser().getName(),
                point.getPoint()
        );
    }
}

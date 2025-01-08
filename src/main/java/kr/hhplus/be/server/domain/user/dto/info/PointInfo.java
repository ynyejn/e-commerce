package kr.hhplus.be.server.domain.user.dto.info;

import kr.hhplus.be.server.domain.user.entity.Point;
import kr.hhplus.be.server.support.exception.ApiException;

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

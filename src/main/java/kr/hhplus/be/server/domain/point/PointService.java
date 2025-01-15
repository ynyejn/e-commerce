package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.user.*;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PointService {
    private final IPointRepository pointRepository;

    /**
     * 사용자 포인트 충전 API
     */
    @Transactional
    public PointInfo chargePoint(User user, PointChargeCommand command) {
        Point point = pointRepository.findByUser(user)
                .orElseGet(() -> pointRepository.save(Point.create(user)));
        point.charge(command.amount());
        point = pointRepository.save(point);
        return PointInfo.from(point);
    }

    /**
     * 사용자 포인트 조회 API
     */
    @Transactional(readOnly = true)
    public PointInfo getPoint(User user) {
        Point point = pointRepository.findByUser(user)
                .orElseGet(() -> Point.create(user));
        return PointInfo.from(point);
    }
}

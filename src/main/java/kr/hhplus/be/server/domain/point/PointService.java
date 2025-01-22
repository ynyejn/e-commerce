package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointService {
    private final IPointRepository pointRepository;

    @Transactional(readOnly = true)
    public PointInfo getPoint(User user) {
        Point point = pointRepository.findByUser(user).orElseGet(() -> Point.create(user));
        return PointInfo.from(point);
    }

    @Transactional
    public PointInfo chargePoint(User user, PointChargeCommand command) {
        Point point = pointRepository.findByUserWithLock(user).orElseThrow(() -> new ApiException(NOT_FOUND));
        point.charge(command.amount());
        point = pointRepository.save(point);
        return PointInfo.from(point);
    }

    @Transactional
    public void use(User user, BigDecimal amount) {
        Point point = pointRepository.findByUserWithLock(user).orElseThrow(() -> new ApiException(NOT_FOUND));
        point.use(amount);
        pointRepository.save(point);
    }

}

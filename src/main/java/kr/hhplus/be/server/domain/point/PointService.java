package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PointInfo charge(PointCommand.Charge command) {
        Point point = pointRepository.findByUserWithLock(command.user()).orElseThrow(() -> new ApiException(NOT_FOUND));
        point.charge(command.amount());
        point = pointRepository.save(point);
        return PointInfo.from(point);
    }

    @Transactional
    public void use(PointCommand.Use command) {
        Point point = pointRepository.findByUserWithLock(command.user()).orElseThrow(() -> new ApiException(NOT_FOUND));
        point.use(command.amount());
        pointRepository.save(point);
    }

}

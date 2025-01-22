package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.user.User;

import java.util.Optional;

public interface IPointRepository {
    Point save(Point point);

    Optional<Point> findByUser(User user);

    Optional<Point> findByUserWithLock(User user);
}

package kr.hhplus.be.server.infra.user;

import kr.hhplus.be.server.domain.user.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointJpaRepository extends JpaRepository<Point, Long> {
    Point save(Point point);
}

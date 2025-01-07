package kr.hhplus.be.server.infra.user.jpa;

import kr.hhplus.be.server.domain.user.entity.Point;
import kr.hhplus.be.server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointJpaRepository extends JpaRepository<Point, Long> {
    Point save(Point point);
}

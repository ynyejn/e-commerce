package kr.hhplus.be.server.infra.point;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<Point, Long> {
    Point save(Point point);

    Optional<Point> findByUser(User user);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT p FROM Point p WHERE p.user = :user")
    Optional<Point> findByUserWithLock(@Param("user") User user);
}

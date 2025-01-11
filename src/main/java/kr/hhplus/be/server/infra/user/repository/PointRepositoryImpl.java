package kr.hhplus.be.server.infra.user.repository;

import kr.hhplus.be.server.domain.user.entity.Point;
import kr.hhplus.be.server.domain.user.repository.IPointRepository;
import kr.hhplus.be.server.infra.user.jpa.PointJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements IPointRepository {
    private final PointJpaRepository pointJpaRepository;

    @Override
    public Point save(Point point) {
        return pointJpaRepository.save(point);
    }
}

package kr.hhplus.be.server.infra.user;

import kr.hhplus.be.server.domain.user.Point;
import kr.hhplus.be.server.domain.user.IPointRepository;
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

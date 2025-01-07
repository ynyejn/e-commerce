package kr.hhplus.be.server.domain.user.service;

import kr.hhplus.be.server.domain.user.dto.command.PointChargeCommand;
import kr.hhplus.be.server.domain.user.dto.info.PointInfo;
import kr.hhplus.be.server.domain.user.entity.Point;
import kr.hhplus.be.server.domain.user.entity.PointHistory;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.IPointHistoryRepository;
import kr.hhplus.be.server.domain.user.repository.IPointRepository;
import kr.hhplus.be.server.domain.user.repository.IUserRepository;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final IPointRepository pointRepository;
    private final IPointHistoryRepository pointHistoryRepository;

    @Transactional
    public PointInfo chargePoint(PointChargeCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new ApiException(NOT_FOUND));

        Point point = user.chargePoint(command.amount());
        return PointInfo.from(pointRepository.save(point));
    }

    @Transactional(readOnly = true)
    public PointInfo getPoint(Long userId) {
        return userRepository.findById(userId)
                .map(user -> Optional.ofNullable(user.getPoint())
                        .map(PointInfo::from)
                        .orElseThrow(() -> new ApiException(ApiErrorCode.NOT_FOUND)))
                .orElseThrow(() -> new ApiException(NOT_FOUND));
    }
}

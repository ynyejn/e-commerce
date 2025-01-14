package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.support.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository userRepository;
    private final IPointRepository pointRepository;

    @Transactional
    public PointInfo chargePoint(PointChargeCommand command) {
        User user = userRepository.findByIdWithPoint(command.userId())
                .orElseThrow(() -> new ApiException(NOT_FOUND));

        Point point = user.chargePoint(command.amount());
        return PointInfo.from(pointRepository.save(point));
    }

    @Transactional(readOnly = true)
    public PointInfo getPoint(Long userId) {
        User user = userRepository.findByIdWithPoint(userId)
                .orElseThrow(() -> new ApiException(NOT_FOUND));

        return Optional.ofNullable(user.getPoint())
                .map(PointInfo::from)
                .orElseThrow(() -> new ApiException(NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<CouponInfo> getCoupons(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(NOT_FOUND));

        return user.getCoupons().stream().
                map(CouponInfo::from).
                collect(Collectors.toList());
    }
}

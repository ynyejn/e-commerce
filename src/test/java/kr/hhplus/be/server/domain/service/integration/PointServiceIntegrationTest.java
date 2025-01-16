package kr.hhplus.be.server.domain.service.integration;

import kr.hhplus.be.server.domain.point.*;
import kr.hhplus.be.server.domain.user.IUserRepository;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"})
class PointServiceIntegrationTest {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IPointRepository pointRepository;
    @Autowired
    private PointService pointService;

    @Test
    void 포인트_충전시_기존잔액에_충전금액이_추가된다() {
        // given
        Long userId = 1L;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));
        BigDecimal initialAmount = BigDecimal.valueOf(100000).setScale(2);
        BigDecimal chargeAmount = BigDecimal.valueOf(50000).setScale(2);

        // when
        PointInfo result = pointService.chargePoint(user, new PointChargeCommand(chargeAmount));

        // then
        assertThat(result.point()).isEqualTo(initialAmount.add(chargeAmount));

        // DB에 실제로 반영되었는지 확인
        Point updatePoint = pointRepository.findByUser(user).orElseThrow();
        assertThat(updatePoint.getPoint()).isEqualTo(initialAmount.add(chargeAmount));
    }

    @Test
    void 포인트_조회시_포인트정보가_정상적으로_조회된다() {
        // given
        Long userId = 1L;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));
        // when
        PointInfo pointInfo = pointService.getPoint(user);

        // then
        assertThat(pointInfo.point()).isEqualTo(BigDecimal.valueOf(100000).setScale(2));
    }

}
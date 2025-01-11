package kr.hhplus.be.server.domain.service.integration;

import kr.hhplus.be.server.domain.coupon.dto.info.CouponInfo;
import kr.hhplus.be.server.domain.user.dto.command.PointChargeCommand;
import kr.hhplus.be.server.domain.user.dto.info.PointInfo;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.IUserRepository;
import kr.hhplus.be.server.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"})
class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private IUserRepository userRepository;


    @Test
    void 포인트_충전시_기존잔액에_충전금액이_추가된다() {
        // given
        Long userId = 1L;
        BigDecimal initialAmount = BigDecimal.valueOf(100000).setScale(2);  // 테스트 데이터의 초기값
        BigDecimal chargeAmount = BigDecimal.valueOf(50000).setScale(2);

        // when
        PointInfo result = userService.chargePoint(new PointChargeCommand(userId, chargeAmount));

        // then
        assertThat(result.point()).isEqualTo(initialAmount.add(chargeAmount));
    }

    @Test
    void 포인트_조회시_포인트정보가_정상적으로_조회된다() {
        // when
        PointInfo pointInfo = userService.getPoint(1L);

        // then
        assertThat(pointInfo.point()).isEqualTo(BigDecimal.valueOf(100000).setScale(2));
    }

    @Test
    void 쿠폰목록_조회시_전체_보유_쿠폰목록이_조회된다() {
        // when
        List<CouponInfo> coupons = userService.getCoupons(2L);

        // then
        assertThat(coupons)
                .hasSize(2)
                .element(0)
                .satisfies(coupon -> {
                    assertThat(coupon.status()).isEqualTo("사용 완료");
                    assertThat(coupon.discountType()).isEqualTo("정률");
                    assertThat(coupon.discountAmount()).isEqualTo(BigDecimal.valueOf(10).setScale(2));
                    assertThat(coupon.usedAt()).isNotNull();
                });
    }
}
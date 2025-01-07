package kr.hhplus.be.server.domain.entity;


import kr.hhplus.be.server.domain.constant.DiscountType;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.CouponIssue;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.within;
import static org.mockito.Mockito.mock;

class CouponIssueTest {

    @Test
    void 쿠폰발급_생성시_만료일이_유효기간만큼_설정된다() {
        // given
        User user = User.create("테스트유저");
        Coupon coupon = Coupon.create("테스트쿠폰", DiscountType.FIXED,
                BigDecimal.valueOf(1000), null,
                LocalDateTime.now(), LocalDateTime.now().plusDays(30), 7);

        // when
        CouponIssue couponIssue = CouponIssue.create(user, coupon);

        // then
        assertThat(couponIssue.getExpiredAt())
                .isCloseTo(LocalDateTime.now().plusDays(7), within(1, ChronoUnit.SECONDS));
    }

    @Test
    void 쿠폰검증시_이미_사용된_쿠폰이면_INVALID_REQUEST_예외가_발생한다() {
        // given
        CouponIssue couponIssue = createCouponIssue();
        Order order = mock(Order.class);
        couponIssue.use(order);

        // when & then
        assertThatThrownBy(() -> couponIssue.validate())
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(ApiErrorCode.INVALID_REQUEST);
    }

    @Test
    void 쿠폰검증시_만료된_쿠폰이면_INVALID_REQUEST_예외가_발생한다() {
        // given
        CouponIssue couponIssue = createCouponIssue();
        ReflectionTestUtils.setField(couponIssue, "expiredAt", LocalDateTime.now().minusDays(1));

        // when & then
        assertThatThrownBy(() -> couponIssue.validate())
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(ApiErrorCode.INVALID_REQUEST);
    }

    @Test
    void 할인금액계산시_정액할인_쿠폰이면_설정된_금액만큼_할인된다() {
        // given
        CouponIssue couponIssue = createFixedCouponIssue(BigDecimal.valueOf(1000));
        BigDecimal orderAmount = BigDecimal.valueOf(10000);

        // when
        BigDecimal discountAmount = couponIssue.calculateDiscountAmount(orderAmount);

        // then
        assertThat(discountAmount).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    void 할인금액계산시_정률할인_쿠폰이면_주문금액기준_할인율만큼_할인된다() {
        // given
        CouponIssue couponIssue = createPercentageCouponIssue(BigDecimal.valueOf(10));
        BigDecimal orderAmount = BigDecimal.valueOf(10000);

        // when
        BigDecimal discountAmount = couponIssue.calculateDiscountAmount(orderAmount);

        // then
        assertThat(discountAmount).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    void 할인금액계산시_주문금액이_최소주문금액보다_작으면_INVALID_REQUEST_예외가_발생한다() {
        // given
        CouponIssue couponIssue = createCouponIssueWithMinimumOrderAmount(BigDecimal.valueOf(10000));
        BigDecimal orderAmount = BigDecimal.valueOf(9000);

        // when & then
        assertThatThrownBy(() -> couponIssue.calculateDiscountAmount(orderAmount))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(ApiErrorCode.INVALID_REQUEST);
    }

    private CouponIssue createCouponIssue() {
        User user = User.create("테스트유저");
        Coupon coupon = Coupon.create("테스트쿠폰", DiscountType.FIXED,
                BigDecimal.valueOf(1000), null,
                LocalDateTime.now(), LocalDateTime.now().plusDays(30), 7);
        return CouponIssue.create(user, coupon);
    }

    private CouponIssue createFixedCouponIssue(BigDecimal discountAmount) {
        User user = User.create("테스트유저");
        Coupon coupon = Coupon.create("정액할인쿠폰", DiscountType.FIXED,
                discountAmount, null,
                LocalDateTime.now(), LocalDateTime.now().plusDays(30), 7);
        return CouponIssue.create(user, coupon);
    }

    private CouponIssue createPercentageCouponIssue(BigDecimal discountRate) {
        User user = User.create("테스트유저");
        Coupon coupon = Coupon.create("정률할인쿠폰", DiscountType.PERCENTAGE,
                discountRate, null,
                LocalDateTime.now(), LocalDateTime.now().plusDays(30), 7);
        return CouponIssue.create(user, coupon);
    }

    private CouponIssue createCouponIssueWithMinimumOrderAmount(BigDecimal minimumOrderAmount) {
        User user = User.create("테스트유저");
        Coupon coupon = Coupon.create("최소주문금액쿠폰", DiscountType.FIXED,
                BigDecimal.valueOf(1000), minimumOrderAmount,
                LocalDateTime.now(), LocalDateTime.now().plusDays(30), 7);
        return CouponIssue.create(user, coupon);
    }
}
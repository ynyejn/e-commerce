package kr.hhplus.be.server.domain.entity;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.Coupon.DiscountType;
import kr.hhplus.be.server.domain.coupon.CouponIssue;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UserTest {
    @Test
    void 발급된_쿠폰을_찾으면_해당_쿠폰이_반환된다() {
        // given
        User user = User.create("테스트유저");
        Coupon coupon = Coupon.create("테스트쿠폰", DiscountType.FIXED, BigDecimal.valueOf(1000), null, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), 10, null);
        CouponIssue couponIssue = CouponIssue.create(user, coupon);

        ReflectionTestUtils.setField(coupon, "id", 1L);
        ReflectionTestUtils.setField(user, "coupons", List.of(couponIssue));

        // when
        CouponIssue found = user.findCouponIssue(coupon.getId());

        // then
        assertThat(found).isEqualTo(couponIssue);
    }

    @Test
    void 발급되지_않은_쿠폰을_찾으면_예외가_발생한다() {
        // given
        User user = User.create("테스트유저");
        ReflectionTestUtils.setField(user, "id", 1L);
        Long notIssuedCouponId = 999L;

        // when & then
        assertThatThrownBy(() -> user.findCouponIssue(notIssuedCouponId))
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(NOT_FOUND);
    }

}
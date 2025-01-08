package kr.hhplus.be.server.domain.entity;

import kr.hhplus.be.server.domain.constant.DiscountType;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.INSUFFICIENT_COUPON;
import static kr.hhplus.be.server.support.exception.ApiErrorCode.INVALID_REQUEST;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CouponTest {
    @Test
    void 쿠폰_발급시_발급_가능_기간이_아니면_INVALID_REQUEST_예외가_발생한다(){
        // given
        Coupon coupon = Coupon.create(
                "테스트쿠폰",
                DiscountType.FIXED,
                BigDecimal.valueOf(1000),
                null,
                LocalDateTime.now().plusDays(1),  // 발급 시작일이 내일
                LocalDateTime.now().plusDays(7),
                30,
                100
        );
        int beforeQuantity = coupon.getIssuedQuantity();

        // when & then
        assertThatThrownBy(() -> coupon.issueCoupon())
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(INVALID_REQUEST);

        assertThat(coupon.getIssuedQuantity()).isEqualTo(beforeQuantity);
    }
    @Test
    void 쿠폰_발급시_발급_가능_수량이_0이면_INSUFFICIENT_COUPON_예외가_발생한다(){
        // given
        Coupon coupon = Coupon.create(
                "테스트쿠폰",
                DiscountType.FIXED,
                BigDecimal.valueOf(1000),
                null,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(7),
                30,
                1  // 최대 발급 수량 1개
        );

        coupon.issueCoupon();
        int beforeQuantity = coupon.getIssuedQuantity();

        // when & then
        assertThatThrownBy(() -> coupon.issueCoupon())
                .isInstanceOf(ApiException.class)
                .extracting("apiErrorCode")
                .isEqualTo(INSUFFICIENT_COUPON);

        assertThat(coupon.getIssuedQuantity()).isEqualTo(beforeQuantity);
    }
}
package kr.hhplus.be.server.domain.service.integration;

import kr.hhplus.be.server.domain.coupon.CouponDiscountInfo;
import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.user.IUserRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.exception.ApiErrorCode;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.INSUFFICIENT_COUPON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"})
class CouponServiceIntegrationTest {
    @Autowired
    private CouponService couponService;
    @Autowired
    private IUserRepository userRepository;

    @Test
    void 쿠폰발급_후_목록_조회시_발급된_쿠폰이_조회된다() {
        // given
        Long userId = 1L;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));

        // when
        // 쿠폰 발급
        CouponInfo issuedCoupon = couponService.issueCoupon(new CouponCommand.Issue(user, 1L));
        // 쿠폰 목록 조회
        List<CouponInfo> coupons = couponService.getCoupons(user);

        // then
        // 발급된 쿠폰 정보 검증
        assertThat(issuedCoupon.couponId()).isEqualTo(1L);
        assertThat(issuedCoupon.discountType()).isEqualTo("정률");
        assertThat(issuedCoupon.discountAmount()).isEqualTo(BigDecimal.valueOf(10).setScale(2));
        assertThat(issuedCoupon.status()).isEqualTo("미사용");

        // 조회된 쿠폰 목록 검증
        assertThat(coupons)
                .isNotEmpty()
                .hasSize(2)
                .element(0)
                .satisfies(coupon -> {
                    assertThat(coupon.couponId()).isEqualTo(1L);
                    assertThat(coupon.status()).isEqualTo("미사용");
                    assertThat(coupon.discountType()).isEqualTo("정률");
                    assertThat(coupon.discountAmount()).isEqualTo(BigDecimal.valueOf(10).setScale(2));
                    assertThat(coupon.usedAt()).isNull();
                });
    }


    @Test
    void 동시에_쿠폰발급시_발급수량을_초과하면_INSUFFICIENT_COUPON_예외가_발생한다() throws InterruptedException {
        // given
        int threadCount = 5;
        Long couponId = 2L;   // 최대 발급수량이 '3'인 쿠폰
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < threadCount; i++) {
            Long userId = (long) (i + 1);  // 각각 다른 사용자 userId = 2부터 5명
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));

            executorService.submit(() -> {
                try {
                    couponService.issueCoupon(new CouponCommand.Issue(user, couponId));
                    successCount.incrementAndGet();
                } catch (ApiException e) {
                    if (e.getApiErrorCode() == INSUFFICIENT_COUPON) {
                        failCount.incrementAndGet();
                    } else {
                        throw e;
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // then
        latch.await(5, TimeUnit.SECONDS);
        assertThat(successCount.get()).isEqualTo(3);  // 발급 가능 수량
        assertThat(failCount.get()).isEqualTo(2);     // 초과 요청 수
    }

    @Test
    void 쿠폰_사용시_할인금액이_정상적으로_계산된다() {
        // given
        Long userId = 3L;
        Long couponIssueId = 2L;  // 미사용 상태의 쿠폰
        BigDecimal orderAmount = BigDecimal.valueOf(100000);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));

        // when
        CouponDiscountInfo discountInfo = couponService.use(new CouponCommand.Use(user, couponIssueId, orderAmount));

        // then
        assertThat(discountInfo.couponIssueId()).isEqualTo(couponIssueId);
        assertThat(discountInfo.discountAmount()).isEqualTo(BigDecimal.valueOf(10000).setScale(2));  // 10% 할인
    }

    @Test
    void 이미_사용한_쿠폰을_사용하면_예외가_발생한다() {
        // given
        Long userId = 2L;
        Long usedCouponIssueId = 1L;  // 이미 사용된 쿠폰
        BigDecimal orderAmount = BigDecimal.valueOf(100000);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));

        // when & then
        assertThatThrownBy(() ->
                couponService.use(new CouponCommand.Use(user, usedCouponIssueId, orderAmount))
        )
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("apiErrorCode", ApiErrorCode.INVALID_REQUEST);
    }

    @Test
    void 동일유저가_이미_발급받은_쿠폰을_재발급시_CONFLICT_예외가_발생한다() {
        // given
        Long userId = 1L;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));
        Long couponId = 1L;
        CouponCommand.Issue command = new CouponCommand.Issue(user, couponId);

        // when
        // 첫 번째 발급 시도
        couponService.issueCoupon(command);

        // then
        // 두 번째 발급 시도시 예외 발생
        assertThatThrownBy(() ->
                couponService.issueCoupon(command)
        )
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("apiErrorCode", ApiErrorCode.CONFLICT);
    }
}
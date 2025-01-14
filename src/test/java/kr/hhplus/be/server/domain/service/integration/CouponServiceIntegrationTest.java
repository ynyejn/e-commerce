package kr.hhplus.be.server.domain.service.integration;

import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponIssueCommand;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.INSUFFICIENT_COUPON;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = {"/cleanup.sql", "/test-data.sql"})
class CouponServiceIntegrationTest {
    @Autowired
    private CouponService couponService;


    @Test
    void 쿠폰_발급이_정상적으로_동작한다() {
        // when
        CouponInfo couponInfo = couponService.issueCoupon(new CouponIssueCommand(1L, 1L));

        // then
        assertThat(couponInfo.couponId()).isEqualTo(1L);
        assertThat(couponInfo.discountType()).isEqualTo("정률");
        assertThat(couponInfo.discountAmount()).isEqualTo(BigDecimal.valueOf(10).setScale(2));
        assertThat(couponInfo.status()).isEqualTo("미사용");

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
            executorService.submit(() -> {
                try {
                    couponService.issueCoupon(new CouponIssueCommand(userId, couponId));
                    successCount.incrementAndGet();
                } catch (ApiException e) {
                    if (e.getApiErrorCode() == INSUFFICIENT_COUPON) {
                        failCount.incrementAndGet();
                    }else{
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
}
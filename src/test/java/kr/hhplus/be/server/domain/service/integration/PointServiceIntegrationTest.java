package kr.hhplus.be.server.domain.service.integration;

import kr.hhplus.be.server.domain.point.*;
import kr.hhplus.be.server.domain.user.IUserRepository;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
        PointInfo result = pointService.charge(new PointCommand.Charge(user, chargeAmount));

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

    @RepeatedTest(10)
    void 동일_사용자가_동시에_포인트_3번_충전시_1번_성공하고_2번은_ObjectOptimisticLockingFailureException_에러가_발생한다() throws InterruptedException {
        // given
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));
        BigDecimal chargeAmount = BigDecimal.valueOf(100);

        int threadCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger conflictCount = new AtomicInteger();

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    pointService.charge(new PointCommand.Charge(user, chargeAmount));
                    successCount.incrementAndGet();
                } catch (ObjectOptimisticLockingFailureException e) {
                    conflictCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        // then
        latch.await(5, TimeUnit.SECONDS);

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(conflictCount.get()).isEqualTo(threadCount - 1);

        executorService.shutdown();
    }

    @RepeatedTest(10)
    void 동일_사용자가_동시에_포인트_3번_사용시_1번_성공하고_2번은_ObjectOptimisticLockingFailureException_에러가_발생한다() throws InterruptedException {
        // given
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("테스트 데이터가 없습니다."));
        BigDecimal useAmount = BigDecimal.valueOf(100);

        int threadCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger conflictCount = new AtomicInteger();

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    pointService.use(new PointCommand.Use(user, useAmount));
                    successCount.incrementAndGet();
                } catch (ObjectOptimisticLockingFailureException e) {
                    conflictCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        // then
        latch.await(5, TimeUnit.SECONDS);

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(conflictCount.get()).isEqualTo(threadCount - 1);

        executorService.shutdown();
    }


}
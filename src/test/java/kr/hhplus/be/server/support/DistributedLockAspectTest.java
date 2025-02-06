package kr.hhplus.be.server.support;

import kr.hhplus.be.server.domain.support.DistributedLock;
import kr.hhplus.be.server.support.aop.DistributedLockAspect;
import kr.hhplus.be.server.support.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import java.util.concurrent.TimeUnit;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.LOCK_ACQUISITION_FAILED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DistributedLockAspectTest {
    private RedissonClient redissonClient;
    private TestService testService;
    private RLock rLock;
    private RLock multiLock;

    @BeforeEach
    void setUp() {
        redissonClient = mock(RedissonClient.class);
        rLock = mock(RLock.class);
        multiLock = mock(RLock.class);

        when(redissonClient.getLock(any(String.class))).thenReturn(rLock);
        when(redissonClient.getMultiLock(any(RLock[].class))).thenReturn(multiLock);

        TestService targetService = new TestService();

        // TestService클래스의 AOP 프록시를 설정
        AspectJProxyFactory factory = new AspectJProxyFactory(targetService);
        factory.addAspect(new DistributedLockAspect(redissonClient));

        // 프록시 객체를 생성
        testService = factory.getProxy();
    }

    @Test
    void 분산락_획득_성공시_정해진_순서대로_실행된다() throws InterruptedException {
        // given
        when(multiLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(true);

        // when
        String result = testService.executeWithLock();

        // then
        InOrder inOrder = inOrder(redissonClient, multiLock);
        inOrder.verify(redissonClient).getLock(any(String.class));
        inOrder.verify(multiLock).tryLock(anyLong(), anyLong(), any());
        inOrder.verify(multiLock).unlock();

        assertThat(result).isEqualTo("success");
    }

    @Test
    void 분산락_획득_실패시_예외가_발생한다() throws InterruptedException {
        // given
        when(multiLock.tryLock(anyLong(), anyLong(), any(TimeUnit.class))).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> testService.executeWithLock())
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("apiErrorCode", LOCK_ACQUISITION_FAILED);

        verify(multiLock).tryLock(anyLong(), anyLong(), any());
        verify(multiLock).unlock();
    }

    @Test
    void 비즈니스로직_실행중_예외발생시_락이_해제되어야한다() throws InterruptedException {
        // given
        when(multiLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> testService.executeWithException())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("비즈니스 로직 실패");

        verify(multiLock).tryLock(anyLong(), anyLong(), any());
        verify(multiLock).unlock();
    }

    static class TestService {
        @DistributedLock(key = "'test'")
        public String executeWithLock() {
            return "success";
        }

        @DistributedLock(key = "'test'")
        public String executeWithException() {
            throw new RuntimeException("비즈니스 로직 실패");
        }
    }
}

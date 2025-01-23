package kr.hhplus.be.server.support;

import kr.hhplus.be.server.domain.support.DistributedLock;
import kr.hhplus.be.server.interfaces.support.aop.DistributedLockAspect;
import kr.hhplus.be.server.support.exception.ApiException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import static kr.hhplus.be.server.support.exception.ApiErrorCode.LOCK_ACQUISITION_FAILED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DistributedLockAspectTest {
    @Mock
    private RedissonClient redissonClient;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private RLock rLock;

    @Mock
    private RLock multiLock;

    @InjectMocks
    private DistributedLockAspect lockAspect;

    @Test
    void 분산락_획득_성공시_정해진_순서대로_실행된다() throws Throwable {
        // given
        DistributedLock distributedLock = createDistributedLockAnnotation();
        MethodSignature signature = createMethodSignature();
        Object expectedResult = new Object();

        when(joinPoint.getSignature()).thenReturn(signature);
        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(redissonClient.getMultiLock(any())).thenReturn(multiLock);
        when(multiLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(joinPoint.proceed()).thenReturn(expectedResult);

        // when
        Object result = lockAspect.executeWithLock(joinPoint, distributedLock);

        // then
        InOrder inOrder = inOrder(redissonClient, multiLock, joinPoint);

        // 1. 락 생성
        inOrder.verify(redissonClient).getLock(anyString());
        inOrder.verify(redissonClient).getMultiLock(any());

        // 2. 락 획득
        inOrder.verify(multiLock).tryLock(anyLong(), anyLong(), any());

        // 3. 비즈니스 로직 실행
        inOrder.verify(joinPoint).proceed();

        // 4. 락 해제
        inOrder.verify(multiLock).unlock();

        // 5. 결과 확인
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void 분산락_획득_실패시_예외가_발생한다() throws Throwable {
        // given
        DistributedLock distributedLock = createDistributedLockAnnotation();
        MethodSignature signature = createMethodSignature();

        when(joinPoint.getSignature()).thenReturn(signature);
        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(redissonClient.getMultiLock(any())).thenReturn(multiLock);
        when(multiLock.tryLock(anyLong(), anyLong(), any())).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> lockAspect.executeWithLock(joinPoint, distributedLock))
                .isInstanceOf(ApiException.class)
                .hasFieldOrPropertyWithValue("apiErrorCode", LOCK_ACQUISITION_FAILED);

        verify(multiLock).tryLock(anyLong(), anyLong(), any());
        verify(joinPoint, never()).proceed();
        verify(multiLock).unlock();
    }

    @Test
    void 비즈니스로직_실행중_예외발생시_락이_해제되어야한다() throws Throwable {
        // given
        DistributedLock distributedLock = createDistributedLockAnnotation();
        MethodSignature signature = createMethodSignature();
        RuntimeException expectedException = new RuntimeException("비즈니스 로직 실패");

        when(joinPoint.getSignature()).thenReturn(signature);
        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(redissonClient.getMultiLock(any())).thenReturn(multiLock);
        when(multiLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(joinPoint.proceed()).thenThrow(expectedException);

        // when & then
        assertThatThrownBy(() -> lockAspect.executeWithLock(joinPoint, distributedLock))
                .isEqualTo(expectedException);

        verify(multiLock).tryLock(anyLong(), anyLong(), any());
        verify(joinPoint).proceed();
        verify(multiLock).unlock();
    }

    private DistributedLock createDistributedLockAnnotation() {
        return new DistributedLock() {
            @Override
            public String key() {
                return "'test'";
            }

            @Override
            public long waitTime() {
                return 5L;
            }

            @Override
            public long leaseTime() {
                return 3L;
            }

            @Override
            public TimeUnit timeUnit() {
                return TimeUnit.SECONDS;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return DistributedLock.class;
            }
        };
    }

    private MethodSignature createMethodSignature() {
        MethodSignature signature = mock(MethodSignature.class);
        when(signature.getParameterNames()).thenReturn(new String[]{});
        return signature;
    }
}
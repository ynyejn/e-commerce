import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';
import exec from 'k6/execution';

// 커스텀 메트릭 정의
const successCounter = new Counter('success_counter');
const failCounter = new Counter('fail_counter');
const successRate = new Rate('success_rate');
const requestDuration = new Trend('request_duration');
const processingDelay = new Trend('processing_delay');
const concurrentUsers = new Counter('concurrent_users');  // 동시 접속자

export const options = {
    scenarios: {
        comprehensive_test: {
            executor: 'ramping-arrival-rate',
            startRate: 10,
            timeUnit: '1s',
            stages: [
                { duration: '10s', target: 500 },    // 급격한 초기 스파이크 (0-10초)
                { duration: '30s', target: 500 },    // 최대 부하 유지 (10-40초)
                { duration: '30s', target: 200 },    // 중간 수준 부하 (40-70초)
                { duration: '2m', target: 200 },     // 지속적 부하 유지 (70초-190초)
                { duration: '30s', target: 0 },      // 부하 감소 (190-220초)
            ],
            preAllocatedVUs: 800,
            maxVUs: 1500,
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<1000'],   // 95%의 요청이 1초 이내 처리
        http_req_failed: ['rate<0.15'],      // 15% 미만의 실패율 허용
        'success_rate': ['rate>0.85'],       // 85% 이상 성공률
    },
};

// 테스트할 쿠폰 ID
const COUPON_ID = 128; // SQL 실행 후 받은 ID로 변경해주세요

// 기본 설정
const BASE_URL = 'http://host.docker.internal:8080';
const TEST_USERS = 100000; // 데이터베이스에 있는 사용자 수

export function setup() {
    console.log(`Starting load test for coupon ID: ${COUPON_ID}`);
    return {
        couponId: COUPON_ID,
    };
}

export default function(data) {
    concurrentUsers.add(1); // 동시 접속자 수 증가
    const userId = Math.floor(Math.random() * TEST_USERS) + 1;

    const authHeader = {
        'USER-ID': `${userId}`,
        'Content-Type': 'application/json',
    };

    // 쿠폰 발급 요청 API 호출
    const url = `${BASE_URL}/api/v1/coupons/${data.couponId}/issue-requests`;
    const startTime = new Date().getTime();
    const response = http.post(url, null, { headers: authHeader });
    const duration = new Date().getTime() - startTime;

    // 응답 검증
    let success = false;

    try {
        const checkResult = check(response, {
            'status is 200': (r) => r.status === 200,
            'response has result field': (r) => r.json().result !== undefined,
        });

        // 응답 결과 기록
        if (checkResult && response.json().result === true) {
            successCounter.add(1);
            successRate.add(1);
        } else {
            failCounter.add(1);
            successRate.add(0);
            console.log(`Error for user ${userId}: Status=${response.status}, Body=${response.body}`);
        }
    } catch (e) {
        failCounter.add(1);
        successRate.add(0);
        console.log(`Exception for user ${userId}: ${e.message}, Response: ${response.body}`);
    }

    // 요청 지속 시간 기록
    requestDuration.add(duration);

    // 각 VU의 요청 이력 출력
    if (exec.scenario.iterationInTest % 100 === 0) {
        console.log(`VU ${exec.vu.idInTest}, Iteration ${exec.scenario.iterationInTest}, User ${userId}, Success: ${success}, Duration: ${duration}ms`);
    }
    concurrentUsers.add(-1); // 동시 접속자 수 감소
}

export function teardown(data) {
    console.log(`Test completed for coupon ID: ${data.couponId}`);
    console.log(`Success count: ${successCounter.value}, Fail count: ${failCounter.value}`);
}
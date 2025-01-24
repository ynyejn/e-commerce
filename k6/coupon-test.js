import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
    stages: [
        { duration: '15s', target: 20 },
        { duration: '15s', target: 20 },
        { duration: '15s', target: 0 },
    ],
};

export default function () {
    const userId = Math.floor(Math.random() * 1000) + 1;

    const headers = {
        'Content-Type': 'application/json',
        'USER-ID': userId.toString(),
    };

    const couponResponse = http.post(
        'http://localhost:8080/api/v1/coupons/1/issue',
        null,
        { headers }
    );

    check(couponResponse, {
        'is status 200': (r) => {
            if (r.status !== 200) {
                console.log(`Failed with status ${r.status}: ${r.body}`);
            }
            return r.status === 200;
        }
    });

    sleep(1);
}
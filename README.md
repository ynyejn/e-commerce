# 이커머스 서비스

## 🗓️ 마일스톤
[Github 마일스톤 링크](https://github.com/users/ynyejn/projects/1)
## 🗺️ 시퀀스 다이어그램
<details>
<summary>잔액 충전 API</summary>
<h2>잔액 충전 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Client as 클라이언트
    participant UserSystem as 사용자

    Client->>UserSystem: 1. 잔액 충전 요청(userId, amount)
    activate UserSystem
    Note over UserSystem: 사용자/금액 유효성 검증

    alt 검증 성공
        UserSystem->>Client: 2. 충전 성공 응답(잔액)
    else 검증 실패
        UserSystem->>Client: 2. 에러 응답
    end
    deactivate UserSystem
```

</details>

<details>
<summary>잔액 조회 API</summary>
<h2>잔액 조회 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Client as 클라이언트
    participant UserSystem as 사용자

    Client->>UserSystem: 1. 잔액 조회 요청(userId)
    activate UserSystem
    Note over UserSystem: 사용자 유효성 검증

    alt 검증 성공
        UserSystem->>Client: 2. 현재 잔액 응답
    else 검증 실패
        UserSystem->>Client: 2. 에러 응답
    end
    deactivate UserSystem
```

</details>

<details>
<summary>상품 조회 API</summary>
<h2>상품 조회 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Client as 클라이언트
    participant ProductSystem as 상품

    Client->>ProductSystem: 1. 상품 조회 요청(productId)
    activate ProductSystem
    Note over ProductSystem: 상품 유효성 검증

    alt 검증 성공
        ProductSystem->>Client: 2. 상품 정보 응답(id,이름,가격,잔여수량)
    else 검증 실패
        ProductSystem->>Client: 2. 에러 응답
    end
    deactivate ProductSystem
```

</details>

<details>
<summary>주문/결제 API</summary>
<h2>주문/결제 통합 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Customer as 클라이언트
    participant OrderSystem as 주문
    participant PaymentSystem as 결제
    participant DataSystem as 데이터플랫폼

    Customer->>OrderSystem: 1. 주문 요청(userId, products)
    activate OrderSystem
    Note over OrderSystem: 상품/재고 검증

    alt 검증 성공
        OrderSystem->>PaymentSystem: 2. 결제 요청(orderId, couponId)
        activate PaymentSystem
        Note over PaymentSystem: 쿠폰/잔액 검증 및 처리

        alt 결제 성공
            PaymentSystem->>OrderSystem: 3a. 결제 성공
            OrderSystem->>Customer: 4. 주문 완료
            OrderSystem-->>DataSystem: 5. 주문/결제 데이터 저장
        else 결제 실패
            PaymentSystem->>OrderSystem: 3b. 결제 실패
            OrderSystem->>Customer: 4. 주문 실패
        end
        deactivate PaymentSystem
    else 검증 실패
        OrderSystem->>Customer: 2. 주문 실패
    end
    deactivate OrderSystem
```

<details>
<summary>a.주문 상세</summary>
<h2>a.주문 상세 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Customer as 클라이언트
    participant OrderSystem as 주문
    participant ProductSystem as 상품

    Customer->>OrderSystem: 1. 주문 생성 요청(userId, products)
    activate OrderSystem

    OrderSystem->>ProductSystem: 2. 상품 정보/재고 확인
    activate ProductSystem
    ProductSystem->>OrderSystem: 3. 상품/재고 확인 완료
    deactivate ProductSystem

    alt 재고 충분
        OrderSystem->>ProductSystem: 4. 재고 할당
        activate ProductSystem
        deactivate ProductSystem
        OrderSystem->>Customer: 5. 주문 생성 완료(CREATED)
    else 재고 부족
        OrderSystem->>Customer: 주문 생성 실패(재고 부족)
    end
    deactivate OrderSystem
```

</details>
<details>
<summary>b.결제 상세</summary>
<h2>b.결제 상세 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Client as 클라이언트
    participant PaymentSystem as 결제
    participant OrderSystem as 주문
    participant CouponSystem as 쿠폰
    participant DataSystem as 데이터플랫폼

    Client->>PaymentSystem: 1. 결제 요청(orderId, couponIssueId)
    activate PaymentSystem

    PaymentSystem->>CouponSystem: 2. 쿠폰 검증 요청
    activate CouponSystem

    alt 쿠폰 유효
        CouponSystem->>PaymentSystem: 3a. 할인 금액 반환
        Note over PaymentSystem: 사용자 잔액 확인

        alt 잔액 충분
            Note over PaymentSystem: 잔액 차감 처리
            PaymentSystem->>OrderSystem: 4. 주문 상태 업데이트 요청
            activate OrderSystem
            OrderSystem->>PaymentSystem: 5. 업데이트 완료
            deactivate OrderSystem
            Note over PaymentSystem,DataSystem: 비동기 데이터 처리
            PaymentSystem-->>DataSystem: 6. 주문 데이터 저장(PAID)
            PaymentSystem->>Client: 7. 결제 성공 응답
        else 잔액 부족
            PaymentSystem->>Client: 4. 결제 실패 응답
        end
    else 쿠폰 무효
        CouponSystem->>PaymentSystem: 3b. 쿠폰 검증 실패
        PaymentSystem->>Client: 4. 쿠폰 오류 응답
    end
    deactivate CouponSystem
    deactivate PaymentSystem
```

</details>


</details>

<details>
<summary>선착순 쿠폰 발급 API</summary>
<h2>선착순 쿠폰 발급 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Client as 클라이언트
    participant CouponSystem as 쿠폰

    Client->>CouponSystem: 1. 쿠폰 발급 요청(userId, couponId)
    activate CouponSystem
    Note over CouponSystem: Lock 획득 및 유효성 검증<br/>(사용자/쿠폰/수량)

    alt 검증 성공
        Note over CouponSystem: 쿠폰 발급 처리
        CouponSystem->>Client: 2a. 발급 성공 응답
    else 검증 실패
        CouponSystem->>Client: 2b. 발급 실패 응답
    end
    deactivate CouponSystem
```

</details>

<details>
<summary>보유 쿠폰 목록 조회 API</summary>
<h2>보유 쿠폰 목록 조회 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Client as 클라이언트
    participant CouponSystem as 쿠폰

    Client->>CouponSystem: 1. 쿠폰 목록 조회 요청(userId)
    activate CouponSystem
    Note over CouponSystem: 사용자 유효성 검증

    alt 사용자 유효
        CouponSystem->>Client: 2. 쿠폰 목록 반환(빈 리스트 or 쿠폰 리스트)
    else 사용자 없음
        CouponSystem->>Client: 2. 에러 응답
    end
    deactivate CouponSystem
```
</details>
<details>
<summary>인기 상품 조회 API</summary>
<h2>인기 상품 조회 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Client as 클라이언트
    participant ProductSystem as 상품
    participant OrderSystem as 주문

    Client->>ProductSystem: 1. 인기 상품 목록 요청
    activate ProductSystem

    ProductSystem->>OrderSystem: 2. 최근 3일 주문 데이터 집계 요청
    activate OrderSystem
    Note over OrderSystem: 상품별 판매량 집계
    OrderSystem->>ProductSystem: 3. 상위 5개 상품 데이터 반환
    deactivate OrderSystem

    ProductSystem->>Client: 4. 인기 상품 목록 반환
    deactivate ProductSystem
```

</details>


## 🖇️ ERD
![img.png](docs/erd.png)

## 📝️ API 명세
<details>
<summary>잔액 충전 API</summary>
<br>
<img src="docs/api/chargebalance.png" alt="Order API Image" />
</details>
<details>
<summary>잔액 조회 API</summary>
<br>
<img src="docs/api/balance.png" alt="Order API Image" />
</details>
<details>
<summary>상품 조회 API</summary>
<br>
<img src="docs/api/product.png" alt="Order API Image" />
</details>
<details>
<summary>주문/결제 API</summary>
<br>
<img src="docs/api/orderapi.png" alt="Order API Image" />
</details>
<details>
<summary>결제 API</summary>
<br>
<img src="docs/api/payment.png" alt="Order API Image" />
</details>
<details>
<summary>선착순 쿠폰 발급 API</summary>
<br>
<img src="docs/api/couponissue.png" alt="Order API Image" />
</details>
<details>
<summary>보유 쿠폰 목록 조회 API</summary>
<br>
<img src="docs/api/coupon.png" alt="Order API Image" />
</details>
<details>
<summary>인기 상품 조회 API</summary>
<br>
<img src="docs/api/popular.png" alt="Order API Image" />
</details>
<br><br>

## Getting Started

### Prerequisites

#### Running Docker Containers

`local` profile 로 실행하기 위하여 인프라가 설정되어 있는 Docker 컨테이너를 실행해주셔야 합니다.

```bash
docker-compose up -d
```
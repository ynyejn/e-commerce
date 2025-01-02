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
    Note over UserSystem: 사용자/금액 유효성 검증
    
    alt 검증 성공
        UserSystem->>Client: 2. 충전 성공 응답(잔액)
    else 검증 실패
        UserSystem->>Client: 2. 에러 응답
    end
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
    Note over UserSystem: 사용자 유효성 검증
    
    alt 검증 성공
        UserSystem->>Client: 2. 현재 잔액 응답
    else 검증 실패
        UserSystem->>Client: 2. 에러 응답
    end
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
    Note over ProductSystem: 상품 유효성 검증
    
    alt 검증 성공
        ProductSystem->>Client: 2. 상품 정보 응답(id,이름,가격,잔여수량)
    else 검증 실패
        ProductSystem->>Client: 2. 에러 응답
    end
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

    Customer->>OrderSystem: 1. 주문 요청(userId, products, couponId)
    Note over OrderSystem: 상품/재고/쿠폰 유효성 검증
    
    alt 주문 유효성 검증 성공
        OrderSystem->>PaymentSystem: 2. 결제 요청
        PaymentSystem->>OrderSystem: 3. 결제 결과 반환
        
        alt 결제 성공
            OrderSystem->>Customer: 4. 주문 완료 알림
            Note over OrderSystem,DataSystem: 비동기 데이터 처리
            OrderSystem-->>DataSystem: 5. 주문 데이터 저장(PAID)
        else 결제 실패
            OrderSystem->>Customer: 4. 주문 실패 알림
        end
    else 주문 유효성 검증 실패
        OrderSystem->>Customer: 2. 주문 실패 알림(재고 부족 등)
    end
```

<details>
<summary>a.주문 상세</summary>
<h2>a.주문 상세 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Customer as 클라이언트
    participant OrderSystem as 주문
    participant ProductSystem as 상품
    participant CouponSystem as 쿠폰
    
    Customer->>OrderSystem: 1. 주문 생성 요청(userId, products, couponId)
    OrderSystem->>ProductSystem: 2. 상품 정보/재고 확인
    ProductSystem->>OrderSystem: 3. 상품/재고 확인 완료
    
    alt 재고 충분
        OrderSystem->>CouponSystem: 4. 쿠폰 유효성 검증
        alt 쿠폰 유효
            CouponSystem->>OrderSystem: 5. 할인 금액 계산 완료
            OrderSystem->>ProductSystem: 6. 재고 할당
            OrderSystem->>Customer: 7. 주문 생성 완료(CREATED, 할인적용가)
        else 쿠폰 무효
            OrderSystem->>Customer: 주문 생성 실패(쿠폰 오류)
        end
    else 재고 부족
        OrderSystem->>Customer: 주문 생성 실패(재고 부족)
    end
```

</details>
<details>
<summary>b.결제 상세</summary>
<h2>b.결제 상세 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant OrderSystem as 주문
    participant PaymentSystem as 결제
    participant DataSystem as 데이터플랫폼
    
    OrderSystem->>PaymentSystem: 1. 결제 요청(orderId)
    Note over PaymentSystem: 사용자 잔액 확인
    
    alt 잔액 충분
        Note over PaymentSystem: 잔액 차감 처리
        PaymentSystem->>OrderSystem: 2a. 결제 성공
        Note over OrderSystem,DataSystem: 비동기 데이터 처리
        OrderSystem-->>DataSystem: 3. 주문 데이터 저장(PAID)
    else 잔액 부족
        PaymentSystem->>OrderSystem: 2b. 결제 실패
        OrderSystem-->>DataSystem: 3. 주문 상태 업데이트(PAYMENT_FAILED)
    end
```

</details>


</details>

<details>
<summary>선착순 쿠폰 발급 API</summary>
<h2>선착순 쿠폰 발급 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Client as 클라이언트
    participant QueueSystem as 대기열
    participant Scheduler as 스케줄러
    participant CouponSystem as 쿠폰
    
    Client->>QueueSystem: 1. 쿠폰 발급 대기열 등록(userId, couponId)
    Note over QueueSystem: UUID 생성 및<br/>요청정보와 매핑
    QueueSystem->>Client: 2. UUID 반환
    
    Note over Scheduler: 1초마다 100개씩<br/>대기열 처리
    Scheduler->>QueueSystem: 3. 대기열 처리
    
    alt 쿠폰 발급 가능
        Scheduler->>CouponSystem: 4a. 쿠폰 발급
        Note over QueueSystem: 상태 업데이트: COMPLETED
    else 쿠폰 발급 불가
        Note over QueueSystem: 상태 업데이트: FAILED
    end
    
    loop Polling
        Client->>QueueSystem: 5. 발급 상태 확인(UUID)
        QueueSystem->>Client: 6. 상태 반환(WAITING/COMPLETED/FAILED)
    end
```

<details>
<summary>a.대기열 등록</summary>
<h2>a.대기열 등록 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
   participant Client as 클라이언트
   participant QueueSystem as 대기열
   participant CouponSystem as 쿠폰
   
   Client->>QueueSystem: 1. 대기열 등록 요청(userId, couponId)
   QueueSystem->>CouponSystem: 2. 쿠폰 발급 가능 여부 확인
   Note over CouponSystem: 쿠폰 유효성 검증<br/>- 발급 기한 확인<br/>- 잔여 수량 확인<br/>- 사용자 대기열 중복 확인
   
   alt 발급 가능
       CouponSystem->>QueueSystem: 3a. 발급 가능 응답
       Note over QueueSystem: UUID 생성 및<br/>대기열 등록
       QueueSystem->>Client: 4a. UUID 반환
   else 발급 불가
       CouponSystem->>QueueSystem: 3b. 발급 불가 응답(기한만료/수량초과/중복신청)
       QueueSystem->>Client: 4b. 에러 응답
   end
```
</details>
<details>
<summary>b.대기열 처리</summary>
<h2>b.대기열 처리 스케줄러 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Scheduler as 스케줄러
    participant QueueSystem as 대기열
    participant CouponSystem as 쿠폰
    
    Note over Scheduler: 1초마다 실행
    
    Scheduler->>QueueSystem: 1. 처리할 대기열 조회(100개)
    QueueSystem->>Scheduler: 2. 대기 목록 반환
    
    loop 대기 항목별 처리
        Scheduler->>CouponSystem: 3. 쿠폰 발급 시도
        
        alt 발급 성공
            CouponSystem->>Scheduler: 4a. 발급 완료
            Scheduler->>QueueSystem: 5a. 상태 업데이트(COMPLETED)
        else 발급 실패
            CouponSystem->>Scheduler: 4b. 발급 실패
            Scheduler->>QueueSystem: 5b. 상태 업데이트(FAILED)
        end
    end
```
</details>
<details>
<summary>c.발급 확인</summary>
<h2>c.발급 확인 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Client as 클라이언트
    participant QueueSystem as 대기열
    
    loop Polling(주기적 확인)
        Client->>QueueSystem: 1. 상태 확인 요청(UUID)
        
        alt 처리 완료
            QueueSystem->>Client: 2a. 상태 반환(COMPLETED/FAILED)
            Note over Client: 폴링 종료
        else 처리 중
            QueueSystem->>Client: 2b. 상태 반환(WAITING)
            Note over Client: 일정 시간 후<br/>재요청
        end
    end
```
</details>
</details>

<details>
<summary>보유 쿠폰 목록 조회 API</summary>
<h2>보유 쿠폰 목록 조회 시퀀스 다이어그램</h2>

```mermaid
sequenceDiagram
    participant Client as 클라이언트
    participant CouponSystem as 쿠폰
    
    Client->>CouponSystem: 1. 쿠폰 목록 조회 요청(userId)
    Note over CouponSystem: 사용자 유효성 검증
    
    alt 사용자 유효
        CouponSystem->>Client: 2. 쿠폰 목록 반환(빈 리스트 or 쿠폰 리스트)
    else 사용자 없음
        CouponSystem->>Client: 2. 에러 응답
    end
```
</details>



## Getting Started

### Prerequisites

#### Running Docker Containers

`local` profile 로 실행하기 위하여 인프라가 설정되어 있는 Docker 컨테이너를 실행해주셔야 합니다.

```bash
docker-compose up -d
```
package kr.hhplus.be.server.interfaces.controller;

import kr.hhplus.be.server.interfaces.controller.docs.UserControllerDocs;
import kr.hhplus.be.server.interfaces.dto.response.CouponResponse;
import kr.hhplus.be.server.interfaces.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    /**
     * 사용자 잔액 충전 API
     */
    @PutMapping("/{userId}/balance")
    public ResponseEntity<UserResponse> chargeBalance(
            @PathVariable Long userId
    ) {
        UserResponse response = new UserResponse(userId, "연예진", 20000L);
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 잔액 조회 API
     */
    @GetMapping("/{userId}/balance")
    public ResponseEntity<UserResponse> getBalance(@PathVariable Long userId) {
        UserResponse response = new UserResponse(userId, "연예진", 20000L);

        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 쿠폰 조회 API
     */
    @GetMapping("/{userId}/coupons")
    public ResponseEntity<List<CouponResponse>> getUserCoupons(@PathVariable Long userId) {
        CouponResponse coupon1 = new CouponResponse(
                1L,
                "FS3DE15DW0",
                "USED",
                "PERCENT",
                BigDecimal.valueOf(10),
                LocalDateTime.parse("2025-01-05 14:00:13"),
                LocalDateTime.parse("2025-01-05 14:00:13"),
                LocalDateTime.now());

        return ResponseEntity.ok(List.of(coupon1));
    }


}

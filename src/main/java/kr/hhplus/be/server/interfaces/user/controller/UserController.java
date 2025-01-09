package kr.hhplus.be.server.interfaces.user.controller;

import kr.hhplus.be.server.domain.coupon.dto.info.CouponInfo;
import kr.hhplus.be.server.domain.user.dto.info.PointInfo;
import kr.hhplus.be.server.domain.user.service.UserService;
import kr.hhplus.be.server.interfaces.user.controller.docs.UserControllerDocs;
import kr.hhplus.be.server.interfaces.coupon.dto.response.CouponResponse;
import kr.hhplus.be.server.interfaces.user.dto.request.PointChargeRequest;
import kr.hhplus.be.server.interfaces.user.dto.response.UserResponse;
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
    private final UserService userService;

    /**
     * 사용자 잔액 충전 API
     */
    @PutMapping("/{userId}/point")
    public ResponseEntity<UserResponse> chargePoint(
            @PathVariable Long userId,
            @RequestBody PointChargeRequest request
    ) {
        PointInfo response = userService.chargePoint(request.toCommand(userId));
        return ResponseEntity.ok(UserResponse.from(response));
    }

    /**
     * 사용자 잔액 조회 API
     */
    @GetMapping("/{userId}/point")
    public ResponseEntity<UserResponse> getPoint(@PathVariable Long userId) {
        UserResponse response = UserResponse.from(userService.getPoint(userId));
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 쿠폰 조회 API
     */
    @GetMapping("/{userId}/coupons")
    public ResponseEntity<List<CouponResponse>> getUserCoupons(@PathVariable Long userId) {
        List<CouponInfo> couponInfos = userService.getCoupons(userId);

        return ResponseEntity.ok(couponInfos.stream()
                .map(CouponResponse::from)
                .toList());
    }


}

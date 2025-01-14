package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController implements CouponControllerDocs {
    private final CouponService couponService;

    /**
     * 쿠폰 발급 API
     */
    @PostMapping("/issue")
    public ResponseEntity<CouponIssueResponse> issueCoupon(@RequestBody CouponIssueRequest request) {
        CouponInfo couponInfo = couponService.issueCoupon(request.toCommand());
        return ResponseEntity.ok(CouponIssueResponse.from(couponInfo));
    }
}

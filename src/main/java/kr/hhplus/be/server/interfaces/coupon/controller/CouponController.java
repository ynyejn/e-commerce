package kr.hhplus.be.server.interfaces.coupon.controller;

import kr.hhplus.be.server.domain.coupon.dto.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.interfaces.coupon.controller.docs.CouponControllerDocs;
import kr.hhplus.be.server.interfaces.coupon.dto.request.CouponIssueRequest;
import kr.hhplus.be.server.interfaces.coupon.dto.response.CouponIssueResponse;
import kr.hhplus.be.server.interfaces.coupon.dto.response.CouponResponse;
import kr.hhplus.be.server.support.response.ResultResponse;
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

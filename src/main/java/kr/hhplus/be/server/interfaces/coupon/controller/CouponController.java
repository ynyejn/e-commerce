package kr.hhplus.be.server.interfaces.coupon.controller;

import kr.hhplus.be.server.interfaces.coupon.controller.docs.CouponControllerDocs;
import kr.hhplus.be.server.interfaces.coupon.dto.request.CouponIssueRequest;
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

    /**
     * 쿠폰 발급 API
     */
    @PostMapping("/issue")
    public ResponseEntity<ResultResponse> issueCoupon(@RequestBody CouponIssueRequest request) {
        ResultResponse response = ResultResponse.success();
        return ResponseEntity.ok(response);
    }
}

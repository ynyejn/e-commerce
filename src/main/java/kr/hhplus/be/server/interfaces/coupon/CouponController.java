package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.interfaces.support.response.ResultResponse;
import kr.hhplus.be.server.support.auth.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController implements CouponControllerDocs {
    private final CouponService couponService;

    /**
     * 사용자 쿠폰 발급 API
     */
    @PostMapping("/{couponId}/issue")
    public ResponseEntity<CouponIssueResponse> issueCoupon(@AuthenticatedUser User user, @PathVariable Long couponId) {
        CouponInfo couponInfo = couponService.issueCoupon(new CouponCommand.Issue(user, couponId));
        return ResponseEntity.ok(CouponIssueResponse.from(couponInfo));
    }

    /**
     * 사용자 쿠폰 발급 요청 API
     */
    @PostMapping("/{couponId}/issue-requests")
    public ResponseEntity<ResultResponse> requestCouponIssue(@AuthenticatedUser User user,
                                                             @PathVariable Long couponId) {
        boolean result = couponService.requestConponIssue(new CouponCommand.Issue(user, couponId));
        return ResponseEntity.ok(result ? ResultResponse.success() : ResultResponse.fail("쿠폰 발급 요청에 실패했습니다."));
    }

    /**
     * 사용자 쿠폰 조회 API
     */
    @GetMapping("/my")
    public ResponseEntity<List<CouponResponse>> getMyCoupons(@AuthenticatedUser User user) {
        List<CouponInfo> couponInfos = couponService.getCoupons(user);
        return ResponseEntity.ok(couponInfos.stream()
                .map(CouponResponse::from)
                .toList());
    }
}

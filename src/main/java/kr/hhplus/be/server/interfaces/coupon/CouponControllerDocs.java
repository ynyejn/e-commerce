package kr.hhplus.be.server.interfaces.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

import java.util.List;

@Tag(name = "coupon", description = "coupon API")
public interface CouponControllerDocs {

    @Operation(
            summary = "쿠폰 발급",
            description = "인증된 사용자에게 지정된 쿠폰을 발급합니다. 발급 가능한 쿠폰인지 수량과 상태를 확인한 후 발급을 진행합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "쿠폰 발급 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CouponIssueResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "couponId": 1,
                                                "status": "미사용",
                                                "discountType": "정액",
                                                "discountAmount": 5000,
                                                "expiredAt": "2024-02-09T23:59:59",
                                                "usedAt": null,
                                                "createdAt": "2024-01-09T10:00:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code": "UNAUTHORIZED",
                                                "message": "인증되지 않은 사용자입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "쿠폰을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code": "NOT_FOUND",
                                                "message": "리소스를 찾을 수 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "쿠폰 중복 발급 시도 또는 수량 부족",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code": "CONFLICT",
                                                "message": "이미 발급된 쿠폰이거나 수량이 부족합니다."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<CouponIssueResponse> issueCoupon(
            @Parameter(hidden = true) User user,
            @Parameter(
                    description = "발급받을 쿠폰의 ID",
                    required = true
            ) Long couponId
    );

    @Operation(
            summary = "내 쿠폰 목록 조회",
            description = "인증된 사용자가 보유한 모든 쿠폰의 목록을 조회합니다. 사용 완료된 쿠폰을 포함한 모든 쿠폰이 조회됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "쿠폰 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CouponResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code": "UNAUTHORIZED",
                                                "message": "인증되지 않은 사용자입니다."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<List<CouponResponse>> getMyCoupons(@Parameter(hidden = true) User user);
}

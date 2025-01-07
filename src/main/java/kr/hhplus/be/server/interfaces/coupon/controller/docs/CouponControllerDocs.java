package kr.hhplus.be.server.interfaces.coupon.controller.docs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.coupon.dto.request.CouponIssueRequest;
import kr.hhplus.be.server.interfaces.order.dto.response.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "coupon", description = "coupon API")
public interface CouponControllerDocs {

    @Operation(
            summary = "쿠폰 발급",
            description = "사용자에게 쿠폰을 발급합니다. 발급 가능한 쿠폰인지 수량과 상태를 확인한 후 발급을 진행합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "쿠폰 발급 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResultResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "result": true,
                                                "message": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code": "USER_NOT_FOUND",
                                                "message": "사용자를 찾을 수 없습니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "쿠폰 발급 불가",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code": "INVALID_PARAMETER",
                                                "message": "발급 가능한 쿠폰이 아닙니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "쿠폰 수량 부족",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code": "COUPON_INSUFFICIENT",
                                                "message": "쿠폰 수량이 모두 소진되었습니다."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ResultResponse> issueCoupon(
            @Parameter(
                    description = "쿠폰 발급 요청 정보 (사용자 ID, 쿠폰 ID)",
                    required = true
            )
            @RequestBody CouponIssueRequest request
    );
}

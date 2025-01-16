package kr.hhplus.be.server.interfaces.point;

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
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "point", description = "point API")
public interface PointControllerDocs {

    @Operation(
            summary = "포인트 충전",
            description = "인증된 사용자의 포인트를 충전합니다. 충전 금액은 0보다 커야 하며, " +
                    "충전 후 총 포인트가 최대 한도(1,000,000)를 초과할 수 없습니다. " +
                    "충전이 완료되면 갱신된 포인트 정보가 반환됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "포인트 충전 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PointResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "userId": 1,
                                                "point": 50000.00,
                                                "updatedAt": "2024-01-15T14:30:00"
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
                    responseCode = "400",
                    description = "잘못된 충전 요청 (음수 금액이거나 한도 초과)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code": "INVALID_REQUEST",
                                                "message": "유효하지 않은 요청입니다."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<PointResponse> chargePoint(
            @Parameter(hidden = true) User user,
            @Parameter(
                    description = "충전 요청 정보",
                    required = true,
                    schema = @Schema(implementation = PointChargeRequest.class)
            ) @RequestBody PointChargeRequest request
    );

    @Operation(
            summary = "포인트 조회",
            description = "인증된 사용자의 현재 포인트 잔액을 조회합니다. " +
                    "포인트 정보가 없는 경우 0으로 초기화된 정보가 반환됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "포인트 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PointResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "userId": 1,
                                                "point": 50000.00,
                                                "updatedAt": "2024-01-15T14:30:00"
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
            )
    })
    ResponseEntity<PointResponse> getPoint(@Parameter(hidden = true) User user);
}
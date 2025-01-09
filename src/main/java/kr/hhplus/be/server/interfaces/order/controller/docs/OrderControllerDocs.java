package kr.hhplus.be.server.interfaces.order.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.order.dto.request.OrderCreateRequest;
import kr.hhplus.be.server.interfaces.order.dto.response.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "order", description = "order API")
public interface OrderControllerDocs {

    @Operation(
            summary = "주문 생성 및 결제 처리",
            description = "상품 주문과 결제를 처리합니다. 주문 생성 후 결제를 시도하며, 결제 실패 시에도 주문은 '미결제' 상태로 생성됩니다.\n\n" +
                    "처리 프로세스:\n" +
                    "1. 주문 생성\n" +
                    "   - 상품 재고 확인\n" +
                    "   - 쿠폰 적용 및 할인 계산\n" +
                    "   - 주문 정보 저장 ('미결제' 상태)\n" +
                    "2. 결제 처리\n" +
                    "   - 사용자 잔액 확인\n" +
                    "   - 결제 진행\n" +
                    "   - 성공 시 주문 상태 '결제완료'로 변경\n" +
                    "   - 실패 시 주문 상태 '미결제' 유지"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주문 및 결제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "orderId": 1,
                                                "orderNumber": "2025010514001345332",
                                                "status": "결제완료",
                                                "amount": 10000,
                                                "quantity": 10,
                                                "createdAt": "2025-01-05T14:00:13"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 주문 요청, 잔액 부족",
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
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "상품을 찾을 수 없음",
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
                    description = "재고 부족",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code": "INSUFFICIENT_STOCK",
                                                "message": "재고가 부족합니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code": "INTERNAL_SERVER_ERROR",
                                                "message": "서버 내부 오류가 발생했습니다."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<OrderResponse> createOrder(
            @Parameter(
                    description = """
                            주문 생성 요청 정보입니다.
                            주문할 상품 정보, 수량, 적용할 쿠폰 정보 등이 포함됩니다.
                            모든 금액은 양수여야 하며, 수량은 1 이상이어야 합니다.
                            """,
                    required = true
            )
            @RequestBody OrderCreateRequest request
    );
}
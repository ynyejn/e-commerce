package kr.hhplus.be.server.interfaces.order.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.order.dto.request.CreatePaymentRequest;
import kr.hhplus.be.server.interfaces.order.dto.response.OrderResponse;
import kr.hhplus.be.server.support.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "payment", description = "payment API")
public interface PaymentControllerDocs {

    @Operation(
            summary = "결제 처리",
            description = """
                    주문에 대한 결제를 처리합니다. 이 API는 다음과 같은 상황에서 사용됩니다:
                    1. 이전 결제 시도가 실패한 주문의 재결제
                    2. 네트워크 오류로 인한 결제 재시도
                    3. 기타 결제만 따로 처리해야 하는 경우
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "결제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class),
                            examples = @ExampleObject(
                                    name = "결제 성공 응답",
                                    value = """
                                            {
                                                "orderId": 1000,
                                                "orderNo": "2025010514001345332",
                                                "status": "결제 완료",
                                                "totalAmount": 10000,
                                                "totalQuantity": 10,
                                                "createdAt": "2025-01-05T14:00:13"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 결제 요청, 잔액 부족",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "code": "INVALID_REQUEST",
                                                "message": "잘못된 요청입니다."
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "주문을 찾을 수 없음",
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
    ResponseEntity<OrderResponse> createPayment(
            @Parameter(
                    description = """
                            주문 ID를 요청해야 하며, 해당 주문의 결제 가능 여부를 확인한 후 결제가 진행됩니다.
                            """,
                    required = true
            )
            @RequestBody CreatePaymentRequest request
    );
}
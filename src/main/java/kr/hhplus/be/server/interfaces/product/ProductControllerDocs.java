package kr.hhplus.be.server.interfaces.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.support.exception.ErrorResponse;
import kr.hhplus.be.server.interfaces.support.response.PageResultResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "product", description = "product API")
public interface ProductControllerDocs {

    @Operation(
            summary = "상품 목록 조회",
            description = "전체 상품 목록을 페이징하여 조회합니다. "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상품 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PageResultResponse.class),
                            examples = @ExampleObject(
                                    value = """
                   {
                       "result": true,
                       "totalPages": 2,
                       "totalCount": 12,
                       "currentCount": 10,
                       "data": [
                           {
                               "productId": 1,
                               "name": "맥북 프로",
                               "price": 2000000,
                               "stock": 100
                           },
                           {
                               "productId": 2,
                               "name": "아이패드",
                               "price": 1000000,
                               "stock": 50
                           }
                       ]
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
    PageResultResponse<List<ProductResponse>> getAllProducts(
            @Parameter(description = "페이지 정보 (페이지 번호, 크기)")
            Pageable pageable
    );

    @Operation(
        summary = "인기 상품 TOP 5 조회", 
        description = "최근 3일간의 주문 데이터를 기반으로 가장 많이 판매된 상위 5개 상품을 조회합니다. 매일 새벽 3시에 데이터가 갱신됩니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "인기 상품 조회 성공",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = PopularProductResponse.class)),
                examples = @ExampleObject(
                    value = """
                        [
                            {
                                "productId": 1,
                                "name": "맥북 프로",
                                "price": 2000000,
                                "salesCount": 150
                            },
                            {
                                "productId": 2,
                                "name": "아이패드",
                                "price": 1000000,
                                "salesCount": 120
                            }
                        ]
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
    ResponseEntity<List<PopularProductResponse>> getTopFivePopularProducts();
}
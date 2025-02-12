package tech.server.reviral.common.config.docs.bussiness

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import tech.server.reviral.api.point.model.dto.PointExchangeResponseDTO
import java.lang.annotation.Inherited

/**
 *packageName    : tech.server.reviral.common.config.docs.bussiness
 * fileName       : TransferExplain
 * author         : joy58
 * date           : 2025-02-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-04        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "다계좌 이체 요청 사용자 목록 조회",
    description = "포인트 전환 사용자 목록 조회를 진행합니다. 목록에는 사용자의 개인정보가 담겨있습니다.",
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(implementation = PointExchangeResponseDTO::class),
                examples = [
                    ExampleObject(
                        name = "통신 성공",
                        description = "다계좌 이체 목록 정보",
                        value = """
                            {
                                "status": 200,
                                "data" : {
                                    "list": [
                                        {
                                            "pointExchangeId": 1,
                                            "bankCode" : "020",
                                            "account": "33331129293921",
                                            "pointValue": 20000,
                                            "name": "홍길동",
                                            "depositMemo": "38928382919",
                                            "withdrawMemo": "리바이럴 포인트"
                                        },
                                        {
                                            "pointExchangeId": 2,
                                            "bankCode" : "020",
                                            "account": "11012129293921",
                                            "pointValue": 13000,
                                            "name": "임꺽정",
                                            "depositMemo": "38928382920",
                                            "withdrawMemo": "리바이럴 포인트"
                                        }
                                    ]
                                },
                                "timestamp": "2025-02-04 11:30:03"
                            }
                        """
                    )
                ]
            )
        ]
    ),ApiResponse(
        responseCode = "500",
        description = "INTERNAL_SERVER_ERROR",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "서버 오류",
                        description = "서버 오류",
                        value = """
                            {
                                "status": 500,
                                "code": "ME0001",
                                "message": "서버 오류",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    )
                ]
            )
        ]
    )
)
annotation class TransferExplain()

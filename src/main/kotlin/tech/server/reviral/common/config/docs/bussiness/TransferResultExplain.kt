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

/**
 *packageName    : tech.server.reviral.common.config.docs.bussiness
 * fileName       : TransferResultExplain
 * author         : joy58
 * date           : 2025-02-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-04        joy58       최초 생성
 */
@Operation(
    summary = "다계좌 이체 결과값 송부",
    description = "다계좌 이체 완료 후, 결과 내역을 송부한다.",
    requestBody = RequestBody(
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "이체 결과 요청 예시",
                        description = "이체 결과값을 전달한다.",
                        value = """
                            [
                              {
                                "bankName": "우리",
                                "bankCode": "110302038283",
                                "price": 20000,
                                "name": "홍길동",
                                "status": "성공",
                                "uniqueKey": "20302919321"
                              },{
                                "bankName": "우리",
                                "bankCode": "110302038383",
                                "price": 18000,
                                "name": "임꺽정",
                                "status": "오류",
                                "uniqueKey": "20302919322"
                              }
                            ]
                        """
                    )
                ]
            )
        ]
    )
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "통신 성공",
                        description = "이체 결과값에 따른 포인트 처리 완료",
                        value = """
                            {
                                "status": 200,
                                "data" : {
                                    "isExchange": true
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
annotation class TransferResultExplain()

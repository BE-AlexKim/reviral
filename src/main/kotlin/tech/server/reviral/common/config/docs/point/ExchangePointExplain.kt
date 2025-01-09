package tech.server.reviral.common.config.docs.point

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import tech.server.reviral.api.campaign.model.dto.CampaignCardResponseDTO
import java.lang.annotation.Inherited

/**
 *packageName    : tech.server.reviral.common.config.docs.point
 * fileName       : ExchangePointExplain
 * author         : joy58
 * date           : 2024-12-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-30        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "사용자 포인트 전환 요청",
    description = "포인트 전환을 신청합니다.",
    requestBody = RequestBody(
        description = "",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "캠페인 포인트 신청 예시",
                        value = """
                                {
                                  "userId": 16,
                                  "exchangeValue": 0
                                }
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
        description = "OK",
        content = [
            Content(
                schema = Schema(implementation = CampaignCardResponseDTO::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "통신 성공",
                        summary = "포인트 전환 신청 성공",
                        value = """
                            {
                                "status": 200,
                                "data": {
                                    "isExchange": true
                                },
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    )
                ]
            )
        ]
    ),
    ApiResponse(
        responseCode = "400",
        description = "BAD_REQUEST",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "사용자 아이디 오류",
                        description = "해당 사용자가 존재하지 않습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "BE0011",
                                "message": "사용자가 존재하지 않습니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "포인트 오류 - 01",
                        description = "포인트 정보가 존재하지 않습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "PE0001",
                                "message": "포인트 정보가 존재하지 않습니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "포인트 오류 - 02",
                        description = "전환 가능한 포인트 액수를 초과했습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "PE0002",
                                "message": "전환 가능한 포인트 액수를 초과했습니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    )
                ]
            )
        ]
    ), ApiResponse(
        responseCode = "500",
        description = "INTERNAL_SERVER_ERROR",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "",
                        description = "",
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
annotation class ExchangePointExplain

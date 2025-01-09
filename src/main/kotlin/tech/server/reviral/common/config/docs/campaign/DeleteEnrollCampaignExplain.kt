package tech.server.reviral.common.config.docs.campaign

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import tech.server.reviral.api.campaign.model.dto.DeleteCampaignEnrollRequestDTO
import tech.server.reviral.api.campaign.model.dto.EnrollProductOrderNoRequestDTO
import java.lang.annotation.Inherited

/**
 *packageName    : tech.server.reviral.common.config.docs.campaign
 * fileName       : DeleteEnrollCampaignExplain
 * author         : joy58
 * date           : 2025-01-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-09        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "사용자 캠페인 참여 취소",
    description = "사용자가 진행신청한 캠페인을 취소합니다.",
    requestBody = RequestBody(
        description = "캠페인 참여 정보 삭제 시스템",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(implementation = DeleteCampaignEnrollRequestDTO::class),
                examples = [
                    ExampleObject(
                        name = "데이터 요청 예시",
                        value = """
                                {
                                  "userId" : 1,
                                  "campaignEnrollId": 1
                                }
                        """
                    ),ExampleObject(
                        name = "데이터 요청 예시(강제 취소)",
                        value = """
                                {
                                  "userId" : 1,
                                  "campaignEnrollId": 1,
                                  "isForcedRevision": true
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
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "통신 성공",
                        value = """
                            {
                                "status": 200,
                                "data": {
                                    "isDeleted": true
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
                        name = "캠페인 삭제 오류",
                        description = "캠페인의 상태값이 APPLY 상태가 아니거나, 강제 삭제여부가 TRUE가 아닐 경우 오류",
                        value = """
                            {
                                "status": 400,
                                "code": "CP0011",
                                "message": "해당 캠페인을 취소할 수 없습니다.",
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
annotation class DeleteEnrollCampaignExplain()

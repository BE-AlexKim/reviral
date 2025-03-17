package tech.server.reviral.config.docs.bussiness

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import java.lang.annotation.Inherited

/**
 *packageName    : tech.server.reviral.common.config.docs.bussiness
 * fileName       : AddCampaignDetailExplain
 * author         : joy58
 * date           : 2025-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-13        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "캠페인 날짜 추가",
    description = "캠페인의 날짜를 추가합니다.",
    requestBody = RequestBody(
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "캠페인 날짜 추가 요청 모델",
                        value = """
                            {
                                "campaignId": 1
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
        description = "요청 성공",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "캠페인 날짜 응답 모델",
                        value = """
                            {
                                "status": 200,
                                "data": {
                                    "isAdded": true
                                },
                                "timestamp": "2025-01-31 22:30:00"
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
annotation class AddCampaignDetailExplain()

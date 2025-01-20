package tech.server.reviral.common.config.docs.campaign

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import tech.server.reviral.api.campaign.model.dto.CampaignCardResponseDTO
import java.lang.annotation.Inherited

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "캠페인 상세 목록 조회",
    description = "캠페인의 상세정보를 조회합니다.",
    parameters = [
        Parameter(name = "campaignDetailsId", description = "캠페인 상세정보 고유일련번호", required = true)
    ]
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
                        summary = "통신 성공 데이터 목록 조회",
                        value = """
                            {
                                "status": 200,
                                "data": {
                                    "campaigns": {
                                        "campaignId": 34,
                                        "campaignTitle": "이윤승테스트",
                                        "campaignStatus": "PROGRESS",
                                        "campaignPrice": 111,
                                        "campaignPoint": 11,
                                        "totalCount": 5,
                                        "joinCount": 0
                                    }
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
                        name = "",
                        description = "",
                        value = """
                            {
                                "status": 400,
                                "code": "",
                                "message": "",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ), ExampleObject(
                        name = "",
                        description = "",
                        value = """
                            {
                                "status": 400,
                                "code": "",
                                "message": "",
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
annotation class SearchCampaignDetailsExplain

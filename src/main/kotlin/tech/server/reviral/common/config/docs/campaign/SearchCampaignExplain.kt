package tech.server.reviral.common.config.docs.campaign

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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
 *packageName    : tech.server.reviral.common.config.docs.campaign
 * fileName       : SearchCampaignExplain
 * author         : joy58
 * date           : 2024-12-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-09        joy58       최초 생성
 */

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "캠페인 목록 조회",
    description = "캠페인의 목록을 조회합니다. <br>" +
            "캠페인의 카테고리, 플랫폼, 상태값을 미입력 시, 모든 데이터가 생성일 기준으로 최신건부터 출력됩니다.",
    parameters = [
        Parameter(name = "category", description = "조회 카테고리(deadline(마감임박),today(오늘오픈),daily(당일구매),time(시간구매)", required = false),
        Parameter(name = "platform", description = "플랫폼 조회(nv(naver),cp(coupang),etc(기타)", required = false),
        Parameter(name = "status", description = "progress(진행중),finish(마감),wait(대기중)", required = false)
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
                    ),ExampleObject(
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
    ),ApiResponse(
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
annotation class SearchCampaignExplain()

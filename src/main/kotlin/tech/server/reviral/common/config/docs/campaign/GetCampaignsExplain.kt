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

/**
 *packageName    : tech.server.reviral.common.config.docs.campaign
 * fileName       : GetCampaignsExplain
 * author         : joy58
 * date           : 2025-01-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-21        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "캠페인 목록 조회",
    description = "캠페인 목록을 조회합니다.<br>" +
            "캠페인의 목록 조회 시, platform 값을 입력하면 해당하는 플랫폼만, 상태값(status)를 입력하면 해당 상태값에 대한" +
            "목록만 조회할 수 있습니다. 캠페인 타이틀 입력 시, 해당 제목의 타이틀 목록을 조회할 수 있습니다.",
    parameters = [
        Parameter(name = "platform", description = "플랫폼 조회(nv(naver),cp(coupang),etc(기타)", required = false),
        Parameter(name = "status", description = "progress(진행중),finish(마감),wait(대기중)", required = false),
        Parameter(name = "campaignTitle", description = "조회할 캠페인의 타이틀 ", required = false),
        Parameter(name = "size", description = "조회할 캠페인의 갯수", required = false),
        Parameter(name = "page", description = "캠페인 목록 페이지", required = false)
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
                                        "total": 20,
                                        "campaigns": [
                                            {
                                                "campaignId": 1,
                                                "campaignStatus": "WAIT",
                                                "startDate": "2025-01-01",
                                                "finishDate": "2025-01-15",
                                                "campaignPlatform": "NAVER",
                                                "campaignTitle": "ggg",
                                                "campaignTotalPrice": 20300,
                                                "totalRecruitCount": 20,
                                                "totalJoinCount": 12,
                                                "createAt": "2025-01-01 06:30:23"
                                            },
                                            {
                                                "campaignId": 2,
                                                "campaignStatus": "WAIT",
                                                "startDate": "2025-01-01",
                                                "finishDate": "2025-01-15",
                                                "campaignPlatform": "NAVER",
                                                "campaignTitle": "고당도 포도 어쩌구 저쩌구",
                                                "campaignTotalPrice": 20300,
                                                "totalRecruitCount": 20,
                                                "totalJoinCount": 12,
                                                "createAt": "2025-01-01 06:30:23"
                                            }
                                        ]
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
annotation class GetCampaignsExplain()

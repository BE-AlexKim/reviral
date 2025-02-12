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
 * fileName       : GetCampaignDetailsExplain
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
    description = "캠페인의 목록을 조회합니다. <br>" +
            "캠페인의 카테고리, 플랫폼, 상태값을 미입력 시, 모든 데이터가 생성일 기준으로 최신건부터 출력됩니다.",
    parameters = [
        Parameter(name = "campaignId", description = "캠페인 일련번호", required = false),
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
                                    "campaignDetails": [
                                        {
                                            "campaignDetailId": 1,
                                            "sellerStatus": "WAIT",
                                            "applyDate": "2025-01-13",
                                            "campaignTitle": "고당도 프리미엄 샤인머스켓 수출용 가정용 선물세트 망고포도 [원산지:국산(경상북도 경산시)]",
                                            "campaignPrice": "NAVER"
                                        },{
                                            "campaignDetailId": 2,
                                            "sellerStatus": "ACTIVE",
                                            "applyDate": "2025-01-18",
                                            "campaignTitle": "ggg",
                                            "campaignPrice": "NAVER"
                                        }
                                    ]
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
annotation class GetCampaignDetailsExplain()

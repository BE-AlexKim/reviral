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
import tech.server.reviral.api.campaign.model.dto.MyCampaignResponseDTO
import java.lang.annotation.Inherited

/**
 *packageName    : tech.server.reviral.common.config.docs.campaign
 * fileName       : SearchMyCampaignExplain
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "마이 캠페인 정보 조회",
    description = "마이 캠페인의 정보 조회합니다.",
    parameters = [
        Parameter(name = "userId", description = "사용자 일련번호", required = false),
    ]
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "OK",
        content = [
            Content(
                schema = Schema(implementation = MyCampaignResponseDTO::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "통신 성공",
                        summary = "통신 성공 데이터 목록 조회",
                        value = """
                            {
                              "status": 200,
                              "data": {
                                "myCampaigns": {
                                  "joinCount": 2,
                                  "progressCount": 0,
                                  "reviewCount": 0,
                                  "inspectCount": 0,
                                  "userInfo": {
                                    "username": "김기현",
                                    "loginId": "rpp0321",
                                    "expectPoint": 54700,
                                    "changeTotalPoint": 0,
                                    "userPoint": 0
                                  },
                                  "myCampaigns": [
                                    {
                                      "campaignId": 17,
                                      "campaignStatus": "APPLY",
                                      "registerDate": "2024-12-13 17:26",
                                      "campaignImgUrl": "https://naver.com",
                                      "campaignTitle": "칠초 무릎담요 극세사 캠핑 비행기 블랭킷"
                                    },
                                    {
                                      "campaignId": 80,
                                      "campaignStatus": "APPLY",
                                      "registerDate": "2024-12-13 17:37",
                                      "campaignImgUrl": "https://shop-phinf.pstatic.net/20231128_159/1701152501960KKRaJ_JPEG/27979767148270199_339439686.jpg?type=m510",
                                      "campaignTitle": "티엔느쿠본 카본 히터 전기 히터 난로 스토브 보관가방증정"
                                    }
                                  ]
                                }
                              },
                              "timestamp": "2024-12-13 18:23:04"
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
annotation class SearchMyCampaignExplain

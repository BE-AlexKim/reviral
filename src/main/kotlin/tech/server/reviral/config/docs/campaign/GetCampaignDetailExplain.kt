package tech.server.reviral.config.docs.campaign

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import tech.server.reviral.api.campaign.model.dto.AdminCampaignDetailResponseDTO
import tech.server.reviral.api.campaign.model.dto.MyCampaignResponseDTO
import java.lang.annotation.Inherited

/**
 *packageName    : tech.server.reviral.common.config.docs.campaign
 * fileName       : GetCampaignDetailExplain
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
    summary = "캠페인의 상세 진행목록 조회(어드민)",
    description = "캠페인 일련번호를 입력하",
    parameters = [
        Parameter(name = "cd_ix", description = "인코딩 캠페인 상세 일련번호", required = false),
    ]
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "OK",
        content = [
            Content(
                schema = Schema(implementation = AdminCampaignDetailResponseDTO::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "통신 성공",
                        summary = "통신 성공 데이터 목록 조회",
                        value = """
                            {
                              "status": 200,
                              "data": {
                                "campaignDetail": {
                                  "campaignId": 2,
                                  "companyName": "test",
                                  "campaignTitle": "ggg",
                                  "startDate": "2024-11-30",
                                  "finishDate": "2025-01-30",
                                  "totalRecruitCount": 123,
                                  "applyDate": "2025-01-18",
                                  "sellerStatus": "COMPLETE",
                                  "userInfo": [
                                    {
                                      "campaignEnrollId": 1,
                                      "userId": 19,
                                      "enrollStatus": "CANCEL",
                                      "username": "이윤승",
                                      "nvId": "",
                                      "cpId": "",
                                      "phone": "01087621211",
                                      "orderUrl": "",
                                      "reviewUrl": "",
                                      "updateAt": "2025-01-22 00:59:02"
                                    },
                                    {
                                      "campaignEnrollId": 2,
                                      "userId": 19,
                                      "enrollStatus": "CANCEL",
                                      "username": "이윤승",
                                      "nvId": "",
                                      "cpId": "",
                                      "phone": "01087621211",
                                      "orderUrl": "",
                                      "reviewUrl": "",
                                      "updateAt": "2025-01-22 00:59:02"
                                    }
                                  ]
                                }
                              },
                              "timestamp": "2025-01-22 00:59:02"
                            }
                        """
                    )
                ]
            )
        ]
    )
)
annotation class GetCampaignDetailExplain()
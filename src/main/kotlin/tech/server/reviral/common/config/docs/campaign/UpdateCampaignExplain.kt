package tech.server.reviral.common.config.docs.campaign

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import java.lang.annotation.Inherited

/**
 *packageName    : tech.server.reviral.common.config.docs.campaign
 * fileName       : UpdateCampaignExplain
 * author         : joy58
 * date           : 2024-12-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-12        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "캠페인 수정",
    description = "어드민 계정 권한으로 캠페인을 등록합니다.",
    requestBody = RequestBody(
        description = "캠페인을 정보를 수정합니다..",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "캠페인 수정 예시 - 당일구매/조합형 옵션상품",
                        value = """
                                {
                                  "campaignId": 12,
                                  "companyName": "몽테르",
                                  "platform": "NAVER",
                                  "category": "DAILY",
                                  "productTitle": "프리미엄 거봉 포도 1kg 2kg 4kg 선물세트 김천 경산 [원산지:국산]",
                                  "campaignImgUrl": "https://shop-phinf.pstatic.net/20230620_296/1687268816503OcDzQ_PNG/2284631376893470_1651916980.png?type=m510",
                                  "campaignLink": "https://smartstore.naver.com/votremonde/products/5719072766",
                                  "campaignPrice": 19900,
                                  "reviewPoint": 500,
                                  "startSaleDateTime": "2025-01-23",
                                  "endSaleDateTime": "2025-01-24",
                                  "optionType": "MULTI",
                                  "options": [
                                    {
                                      "optionTitle": "고당도 샤인머스켓",
                                      "recruitPeople": 300,
                                      "subOption": [
                                        {
                                          "subOptionTitle": "선물강추 수출용 샤인머스켓 2kg(3수)",
                                          "addPrice": 0,
                                          "recruitPeople": 100
                                        },
                                        {
                                          "subOptionTitle": "선물강추 수출용 샤인머스켓 2kg(6수)",
                                          "addPrice": 17000,
                                          "recruitPeople": 100
                                        },
                                        {
                                          "subOptionTitle": "수출용 샤인머스켓 2kg(4수)",
                                          "addPrice": -1000,
                                          "recruitPeople": 100
                                        }
                                      ]
                                    }
                                  ],
                                  "sellerRequest": "3수 샤인머스켓 소진시, 수출용 샤인머스켓으로 진행해주세요.",
                                  "sellerGuide": "ggkkdd"
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
                                    "isUpdated": true
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
                        name = "캠페인 시작일이 더 과거여야 합니다.",
                        description = "캠페인 시작일과 종료일은 시작일이 이 더 빨라야합니다.",
                        value = """
                            {
                              "status": 400,
                              "code": "CP0002",
                              "message": "캠페인 시작일과 종료일은 시작일이 이 더 빨라야합니다.",
                              "timestamp": "2024-12-05 12:52:50"
                            }
                        """
                    )
                ]
            )
        ]
    ), ApiResponse(
        responseCode = "401",
        description = "UNAUTHORIZED",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "권한이 존재하지 않습니다.",
                        description = "사용자가 어드민 권한이 존재하지 않습니다.",
                        value = """
                            {
                                "status": 401,
                                "code": "BE0006",
                                "message": "권한이 존재하지 않습니다.",
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
annotation class UpdateCampaignExplain

package tech.server.reviral.common.config.docs.campaign

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
 *packageName    : tech.server.reviral.common.config.docs.campaign
 * fileName       : EnrollCampaignExplain
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
    summary = "캠페인 참여 정보 등록",
    description = "캠페인 참여 정보를 등록합니다.",
    requestBody = RequestBody(
        description = "캠페인을 참여합니다.<br>" +
                "아래의 예시 데이터가 삽입되어 있을 경우, 중복에러가 발생할 가능성이 있습니다.",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "캠페인 참여 예시",
                        value = """
                                {
                                  "userId": 16,
                                  "campaignId": 80,
                                  "campaignOptionId": 122,
                                  "campaignSubOptionId": null
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
                        summary = "캠페인 참여 성공",
                        value = """
                            {
                                "status": 200,
                                "data": {
                                    "isSave": true
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
                    ), ExampleObject(
                        name = "캠페인 아이디 오류",
                        description = "해당 캠페인 아이디로 조회된 캠페인이 없습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "CP0004",
                                "message": "해당 캠페인이 존재하지 않습니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "캠페인 중복 오류",
                        description = "중복으로 진행중인 캠페인이 있습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "CP0005",
                                "message": "해당 캠페인을 완료 후, 신청해 주세요.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "캠페인 옵션 오류",
                        description = "캠페인 옵션 아이디가 없습니다. 필수값으로 입력해주세요.",
                        value = """
                            {
                                "status": 400,
                                "code": "CP0003",
                                "message": "옵션을 선택해주세요.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "캠페인 하위 옵션 아이디 오류 - 01",
                        description = "하위 옵션 아이디값이 존재하지 않습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "CP0006",
                                "message": "하위 옵션을 선택해주세요.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "캠페인 하위 옵션 아이디 오류 - 02",
                        description = "해당 캠페인 하위 옵션값이 있으나, 해당 캠페인과는 맞지 않는 값입니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "CP0007",
                                "message": "해당 옵션에 맞지 않는 하위 옵션값입니다.",
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
annotation class EnrollCampaignExplain

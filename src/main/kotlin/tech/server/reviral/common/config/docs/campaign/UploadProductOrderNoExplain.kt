package tech.server.reviral.common.config.docs.campaign

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import tech.server.reviral.api.campaign.model.dto.EnrollProductOrderNoRequestDTO
import java.lang.annotation.Inherited

/**
 *packageName    : tech.server.reviral.common.config.docs.campaign
 * fileName       : UploadProductOrderNoExplain
 * author         : joy58
 * date           : 2025-01-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-08        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "사용자 주문번호 이미지 등록",
    description = "회원가입 시, 사용자가 주문한 주문번호 이미지를 등록합니다.",
    requestBody = RequestBody(
        description = "주문번호 이미지 등록 서비스",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = Schema(implementation = EnrollProductOrderNoRequestDTO::class),
                examples = [
                    ExampleObject(
                        name = "회원가입 정보 예시",
                        value = """
                                {
                                  "userId" : 1,
                                  "campaignEnrollId": 1,
                                  "orderImage": "203029841828332"
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
                        name = "캠페인 참여 오류",
                        description = "해당 캠페인 참여 일련번호가 존재하지 않습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "CP0008",
                                "message": "해당 캠페인은 참여중이지 않습니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "캠페인 참여 오류",
                        description = "해당 캠페인 참여 일련번호가 존재하지 않습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "CP0008",
                                "message": "해당 캠페인은 참여중이지 않습니다.",
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
annotation class UploadProductOrderNoExplain()

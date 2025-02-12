package tech.server.reviral.common.config.docs.account

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
 *packageName    : tech.server.reviral.common.config.docs.account
 * fileName       : PhoneAuthorizeExplain
 * author         : joy58
 * date           : 2025-02-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-08        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "휴대폰 인증번호 발송 및 검증 API",
    description = "<p> 휴대폰 인증번호 발송 및 검증 API 입니다 </p>" +
            "<p>휴대폰 인증번호 발송(send) 관련 설명</p>" +
            "<p>- 휴대폰 인증번호 발송은 Body 요청에 phone 값만을 설정하여 보내면 됩니다 </p>" +
            "<p>휴대폰 인증번호 검증(valid) 관련 설명</p>" +
            "<p>- 휴대폰 인증번호 발송은 Body 요청에 phone 과 code 값을 담아서 요청해주시면 됩니다.</p>" +
            "<p><strong>중요!!!! phone 값은 (-를 제외한 값) 입니다.</strong></p>",
    parameters = [
        Parameter(name = "type", description = "타입이 발송인지 검증인지에 대한 타입 값(valid,send)", required = true)
    ],
    requestBody = RequestBody(
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "휴대폰 인증번호 발송 예제",
                        description = "type이 send일때 위의 데이터를 보내면 됩니다.",
                        value = """
                            {
                                "phone": "01012345678"
                            }
                        """
                    ),ExampleObject(
                        name = "휴대폰 인증번호 검증 예제",
                        description = "type이 valid일때 위의 데이터를 보내면 됩니다.",
                        value = """
                            {
                                "phone": "01012345678",
                                "code": "123456"
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
                        name = "휴대폰 인증번호 발송 성공",
                        description = "휴대폰 인증번호 발송 성공",
                        value = """
                            {
                                "status": 200,
                                "data": {
                                    "isSend": true
                                },
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "휴대폰 인증번호 검증 성공",
                        description = "휴대폰 인증번호 검증 성공",
                        value = """
                            {
                                "status": 200,
                                "data": {
                                    "isValid": true
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
annotation class PhoneAuthorizeExplain()

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
 * fileName       : ValidationPasswordExplain
 * author         : joy58
 * date           : 2025-01-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-09        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "비밀번호를 검증합니다.",
    description = "비밀번호 검증이 필요한 페이지 접근 시, 해당 API를 사용하여 비밀번호를 검증합니다.",
    parameters = [
        Parameter(name = "type", description = "login(로그인 패스워드), point(포인트 패스워드)")
    ],
    requestBody = RequestBody(
        description = "비밀번호를 검증합니다.",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "비밀번호 검증 데이터 예시",
                        value = """
                                {
                                  "userId": 1,
                                  "loginPw": "비밀번호"
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
                        name = "비밀번호 검증 유무",
                        description = "비밀번호 검증 유무",
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
    ),ApiResponse(
        responseCode = "400",
        description = "BAD_REQUEST",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "포인트 비밀번호 설정 오류",
                        description = "포인트 비밀번호가 설정되지 않았습니다.",
                        value = """
                            {
                                "status": 500,
                                "code": "BE0019",
                                "message": "포인트 비밀번호를 설정해주세요.",
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
                        name = "서버 오류",
                        description = "서버가 정상적으로 작동하지 않습니다.",
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
annotation class ValidationPasswordExplain()

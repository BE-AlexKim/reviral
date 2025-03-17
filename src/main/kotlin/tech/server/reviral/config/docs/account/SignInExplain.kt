package tech.server.reviral.config.docs.account

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import java.lang.annotation.Inherited

/**
 *packageName    : tech.server.reviral.common.config.docs.account
 * fileName       : SignInExplain
 * author         : joy58
 * date           : 2024-11-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-28        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "사용자 로그인",
    description = "아이디와 패스워드를 받아 로그인 토큰을 발급한다.",
    requestBody = RequestBody(
        description = "사용자 로그인",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "회원가입 테스트 계정 - 김기현",
                        value = """
                                {
                                  "loginId" : "rpp0321",
                                  "password" : "rlgus585"
                                }
                        """
                    ),ExampleObject(
                        name = "회원가입 테스트 계정 - 이윤승",
                        value = """
                                {
                                  "loginId" : "hpppower111",
                                  "password" : "asdfQWER12#$"
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
                                    "grantType": "Bearer",
                                    "accessToken": "엑세스 토큰",
                                    "refreshToken": "리프레시 토큰"
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
                                "code": "BE0011",
                                "message": "사용자가 존재하지 않습니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ), ExampleObject(
                        name = "",
                        description = "",
                        value = """
                            {
                                "status": 400,
                                "code": "BE0013",
                                "message": ""비밀번호가 일치하지 않습니다.",
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
annotation class SignInExplain()

package tech.server.reviral.config.docs.oauth

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
 *packageName    : tech.server.reviral.common.config.docs.oauth
 * fileName       : OAuthSignupExplain
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
    summary = "소셜 로그인의 인증과 사용자 정보를 가져온다.",
    description = "소셜 로그인의 인증토큰을 발급받고 사용자의 정보를 가져와 회원가입 또는 로그인을 진행한다. <br>" +
            "프론트 서버는 각각의 서버에서 authorize_code 값을 받아와서 해당 요청에 code 값을 넣으면 인증이 처리되면서 <br>" +
            "회원가입 또는 로그인이 진행되고 응답값(hasUserInfo) 값에 따라서 추가정보를 기입 또는 받지 않을 수 있다. ",
    parameters = [
        Parameter(name = "code", description = "각각의 소셜 매체에서 로그인 성공 후, 발급받은 인가코드 값", required = true),
        Parameter(name = "provider", description = "각각의 소셜 제공업체 명(kakao,naver)", required = true)
    ],
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
                        name = "OAuth 로그인 통신 성공",
                        description = "OAuth 회원정보 조회 후, 가입되어 있는지 아닌지에 따라 데이터를 저장한다.",
                        value = """
                            {
                                "status": 200,
                                "data": {
                                    "jwt": {
                                        "accessToken": "엑세스 토큰",
                                        "refreshToken": "리프레시 토큰",
                                        "grantType": "토큰타입",
                                        "hasUserInfo": true
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
        responseCode = "400",
        description = "BAD_REQUEST",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "잘못된 데이터를 입력해서 요청한 경우",
                        description = "oauth client 오류",
                        value = """
                            {
                                "status": 400,
                                "code": "BE0025",
                                "message": "잘못된 OAUTH 요청입니다",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "객체의 타입이 일치하지 않습니다.",
                        description = "응답된 타입이 일치하지 않아서 나는 오류",
                        value = """
                            {
                                "status": 400,
                                "code": "BE0026",
                                "message": "응답 타입이 일치하지 않습니다.",
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
                    ),ExampleObject(
                        name = "네트워크 연결 지연 오류",
                        description = "소셜 서버 네트워크 지연 오류",
                        value = """
                            {
                                "status": 500,
                                "code": "BE0026",
                                "message": "네트워크 연결 지연 문제",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    )
                ]
            )
        ]
    )
)
annotation class OAuthSignupExplain

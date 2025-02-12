package tech.server.reviral.common.config.docs.oauth

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
 * fileName       : OAuthUnlinkExplain
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
    summary = "해당 사용자의 소셜 연동 끊기",
    description = "해당 사용자의 사용자 아이디 또는 엑세스토큰으로 해당 소셜에 연결 끊기를 요청하여 회원을 탈퇴시킨다.",
    parameters = [
        Parameter(name = "provider", description = "각각의 소셜 제공업체 명(kakao,naver)", required = true)
    ],
    requestBody = RequestBody(
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "소셜 연결끊기 요청 데이터 예시",
                        description = "사용자 일련번호 입력하여 연동 및 회원탈퇴 처리",
                        value = """
                            {
                                "userId": 1
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
                    ), ExampleObject(
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
                    ), ExampleObject(
                        name = "지원하지 않는 인증방법입니다.",
                        description = "Kakao, Naver 이외의 로그인 인증 요청 시, 오류 발생",
                        value = """
                            {
                                "status": 400,
                                "code": "BE0022",
                                "message": "지원하지 않는 소셜 제공업체입니다.",
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
                    ), ExampleObject(
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
annotation class OAuthUnlinkExplain()

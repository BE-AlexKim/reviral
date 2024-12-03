package tech.server.reviral.common.config.docs.account

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
 * fileName       : JwtReloadExplain
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
    summary = "사용자 재인증",
    description = "사용자 재인증 토큰으로 새로운 토큰을 발급한다.",
    requestBody = RequestBody(
        description = "회원 정보를 등록합니다.",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "재발급 테스트 - 01",
                        value = """
                                {
                                  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiaXNzIjoiUkU6VklSQUwuQ08iLCJpYXQiOjE3MzI3ODU3OTcsInVzZXJuYW1lIjoicnBwMDMyMSIsInJvbGVzIjoiUk9MRV9BRE1JTiIsImV4cCI6MTczMjc5Mjk5N30.Jh57RS2o9aMS2iywkJ-OGIh4CkHgJUIv06twTuAWpho",
                                  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjYyMjEyNzg1Nzk3fQ.VTdZRWmbKnz4YqE1aPHZAFU7b2_0XBWF1PdhAZMyQHc"
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
        responseCode = "401",
        description = "UNAUTHORIZED",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "",
                        description = "",
                        value = """
                            {
                                "status": 401,
                                "code": "BE0008",
                                "message": "만료된 접근입니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ), ExampleObject(
                        name = "재발급 토큰 불일치 오류",
                        description = "리프레시 토큰 불일치",
                        value = """
                            {
                                "status": 401,
                                "code": "BE0009",
                                "message": "잘못된 접근입니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ), ExampleObject(
                        name = "존재하지 않는 토큰",
                        description = "엑세스 토큰이 존재하지 않을 경우 발생합니다.",
                        value = """
                            {
                                "status": 401,
                                "code": "BE0007",
                                "message": "잘못된 접근입니다.",
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
annotation class JwtReloadExplain()

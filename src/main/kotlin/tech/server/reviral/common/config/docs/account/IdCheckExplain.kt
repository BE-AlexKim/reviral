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
 * fileName       : SignUpExplain
 * author         : joy58
 * date           : 2024-11-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-27        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "사용자 로그인 아이디 중복체크",
    description = "회원가입 시, 사용자 아이디 중복여부를 확인할 수 있다.",
    parameters = [
        Parameter(
            name = "loginId", description = "로그인 아이디", required = true, example = "rpp0321"
        )
    ]
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
                        summary = "아이디가 중복됨",
                        description = "아이디 중복되었습니다.",
                        value = """
                            {
                                "status": 200,
                                "data": {
                                    "isDuplicated": true
                                },
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "통신 성공",
                        summary = "아이디 중복안됨",
                        description = "아이디가 중복되지 않었습니다.",
                        value = """
                            {
                                "status": 200,
                                "data": {
                                    "isDuplicated": false
                                },
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    )
                ]
            )
        ]
    )
)
annotation class IdCheckExplain()

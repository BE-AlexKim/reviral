package tech.server.reviral.config.docs.account

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import tech.server.reviral.config.response.exception.enums.BasicError
import tech.server.reviral.config.response.exception.message.BasicExceptionMessage
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
    summary = "사용자 회원가입",
    description = "회원가입 시, 사용자 정보를 요청하여 회원 정보를 등록한다.",
    requestBody = RequestBody(
        description = "회원 정보를 등록합니다.",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "회원가입 정보 예시",
                        value = """
                                {
                                  "loginId": "reviral001",
                                  "loginPw": "Reviral001#",
                                  "username": "홍길동",
                                  "gender": "MAN",
                                  "phoneNumber": "01012341234",
                                  "address": "서울특별시 중랑구 공릉로12가길 15 지하1층",
                                  "nvId": "joy585",
                                  "cpId": "rpp0321@gmail.com",
                                  "isEvent": true
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
                                    "signUp": true
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
                        name = "비밀번호 유효성 - 01",
                        description = "비밀번호 값은 필수값입니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "HV000033",
                                "message": "비밀번호를 입력해주세요.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "비밀번호 유효성 - 02",
                        description = "비밀번호 형식이 일치하지 않습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "HV000033",
                                "message": "비밀번호 형식이 맞지 않습니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "아이디 유효성 - 01",
                        description = "아이디는 필수값입니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "HV000033",
                                "message": "아이디를 입력해주세요.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "아이디 유효성 - 02",
                        description = "대문자는 입력할 수 없습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "HV000033",
                                "message": "영문 대문자는 포함할 수 없습니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "아이디 유효성 - 03",
                        description = "아이디가 형식에 맞지 않습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "HV000033",
                                "message": "아이디는 영문 숫자 4-16자 사이로 입력해주세요.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "휴대전화 유효성 - 01",
                        description = "휴대전화번호 형식이 맞지 않습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "HV000033",
                                "message": "휴대전화번호 형식이 맞지 않습니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "이름 유효성 - 01",
                        description = "이름 형식이 알맞지 않습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "HV000033",
                                "message": "이름 형식이 맞지 않습니다.",
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
                        name = BasicExceptionMessage.DEFAULT,
                        description = BasicExceptionMessage.DEFAULT,
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
annotation class SignUpExplain()

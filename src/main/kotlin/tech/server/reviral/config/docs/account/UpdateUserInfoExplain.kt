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
 * fileName       : UpdateUserInfoExplain
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
    summary = "사용자 개인정보 수정 요청 API",
    description = "사용자의 개인정보를 수정 요청합니다.",
    requestBody = RequestBody(
        description = "개인정보 수정 요청 데이터",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "비밀번호 변경 요청",
                        summary = "비밀번호 변경 요청 데이터",
                        description = "사용자 일련번호, 변경할 패스워드를 입력합니다.",
                        value = """
                            {
                                "userId": 1,
                                "loginPw": "변경할 비밀번호"
                            }
                        """
                    ),ExampleObject(
                        name = "포인트 전환 비밀번호 변경 요청",
                        summary = "포인트 비밀번호 변경 예시",
                        description = "사용자의 포인트 전환 비밀번호를 변경합니다.",
                        value = """
                            {
                                "userId": 1,
                                "pointPw": "변경할 비밀번호"
                            }
                        """
                    ),ExampleObject(
                        name = "휴대폰 번호 변경 요청",
                        summary = "휴대폰 번호 변경 요청 예시",
                        description = "사용자의 휴대폰 번호를 변경합니다..",
                        value = """
                            {
                                "userId": 1,
                                "phoneNumber": "변경할 휴대폰번호(공백없이 또는 - 없이)"
                            }
                        """
                    ),ExampleObject(
                        name = "네이버 아이디 변경 요청",
                        summary = "네이버 아이디를 변경합니다.",
                        description = "사용자의 네이버 아이디를 변경합니다.",
                        value = """
                            {
                                "userId": 1,
                                "nvId": "변경할 네이버 아이디"
                            }
                        """
                    ),ExampleObject(
                        name = "쿠팡 아이디 변경 요청",
                        summary = "쿠팡 아이디를 변경합니다.",
                        description = "사용자의 쿠팡 아이디를 변경합니다.",
                        value = """
                            {
                                "userId": 1,
                                "cpId": "변경할 네이버 아이디"
                            }
                        """
                    ),ExampleObject(
                        name = "사용자 주소 변경 요청",
                        summary = "쿠팡 주소를 변경합니다.",
                        description = "사용자의 주소를 변경합니다.",
                        value = """
                            {
                                "userId": 1,
                                "address": "변경할 네이버 아이디"
                            }
                        """
                    ),ExampleObject(
                        name = "사용자 계좌번호 변경",
                        summary = "사용자의 계좌번호를 변경 요청합니다.",
                        description = "사용자 계좌번호를 변경합니다.",
                        value = """
                            {
                                "userId": 1,
                                "bankCode": "변경할 은행코드",
                                "accountNumber": "변경할 계좌번호"
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
                                    "isUpdated": true
                                },
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
annotation class UpdateUserInfoExplain()

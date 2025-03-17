package tech.server.reviral.config.docs.account

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
 * fileName       : UserInfoExplain
 * author         : joy58
 * date           : 2025-01-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-02        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "사용자 개인정보 내역 조회",
    description = "사용자 개인정보 조회",
    parameters = [
        Parameter(
            name = "userId", description = "사용자 일련번호", required = true,
            examples = [
                ExampleObject(
                    name = "예시 데이터",
                    description = "사용자 아이디를 PathVariable 형태로 넣어주세요.",
                    summary = "사용자 예시 데이터 ",
                    value = "1"
                )
            ]
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
                        name = "통신 성공(계좌정보가 없는 경우)",
                        description = "사용자 계좌정보가 없을 경우",
                        value = """
                            {
                                "status": 200,
                                "data": {
                                    "userInfo": {
                                      "phoneNumber": "010-4603-7***",
                                      "address": "경기도 구리시 갈매중앙로25 603-505",
                                      "bankCode": null,
                                      "accountNumber": null
                                    }
                                },
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "통신 성공(계좌정보가 있는 경우)",
                        description = "사용자 계좌정보가 있는 경우",
                        value = """
                            {
                                "status": 200,
                                "data": {
                                    "userInfo": {
                                      "phoneNumber": "010-4603-7***",
                                      "address": "경기도 구리시 갈매중앙로25 603-505",
                                      "bankCode": "090",
                                      "accountNumber": "33331126*****"
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
                        name = "사용자가 존재하지 않습니다.",
                        description = "넘어온 사용자의 아이디가 없는 경우",
                        value = """
                            {
                                "status": 400,
                                "code": "BE0011",
                                "message": "사용자가 존재하지 않습니다.",
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
annotation class UserInfoExplain

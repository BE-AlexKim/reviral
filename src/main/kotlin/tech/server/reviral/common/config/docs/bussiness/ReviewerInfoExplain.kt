package tech.server.reviral.common.config.docs.bussiness

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import java.lang.annotation.Inherited

/**
 *packageName    : tech.server.reviral.common.config.docs.bussiness
 * fileName       : ReviewerInfoExplain
 * author         : joy58
 * date           : 2025-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-13        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "리뷰어 목록 정보 조회",
    description = "회원 목록 정보 조회를 위한 API 입니다."
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
                              "userId": 12345,
                              "name": "홍길동",
                              "loginId": "hong123",
                              "phoneNumber": "010-1234-5678",
                              "nvId": "naver_hong",
                              "cpId": "coupang_hong",
                              "address": "서울특별시 강남구 테헤란로 123",
                              "bankCode": "090",
                              "accountNumber": "12345678901234"
                            }
                        """
                    )
                ]
            )
        ]
    )
)
annotation class ReviewerInfoExplain()

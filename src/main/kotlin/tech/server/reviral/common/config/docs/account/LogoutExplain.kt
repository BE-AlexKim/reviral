package tech.server.reviral.common.config.docs.account

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ExampleObject
import java.lang.annotation.Inherited

/**
 *packageName    : tech.server.reviral.common.config.docs.account
 * fileName       : LogoutExplain
 * author         : joy58
 * date           : 2025-01-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-03        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "사용자 로그아웃",
    description = "사용자 로그아웃 토큰 만료처리",
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
annotation class LogoutExplain()

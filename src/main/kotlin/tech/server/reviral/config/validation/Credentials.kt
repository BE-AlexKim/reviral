package tech.server.reviral.config.validation

import jakarta.validation.Constraint
import kotlin.reflect.KClass

/**
 *packageName    : tech.server.reviral.common.config.validation
 * fileName       : Credentials
 * author         : joy58
 * date           : 2024-11-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-27        joy58       최초 생성
 */
@MustBeDocumented
@Constraint(validatedBy = [CredentialValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Credentials(
    val message: String = "유효하지 않은 입력입니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)




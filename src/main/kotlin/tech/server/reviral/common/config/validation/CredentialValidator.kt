package tech.server.reviral.common.config.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 *packageName    : tech.server.reviral.common.config.validation
 * fileName       : CredentialValidator
 * author         : joy58
 * date           : 2024-11-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-27        joy58       최초 생성
 */
class CredentialValidator: ConstraintValidator<Credentials, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {

        if ( value.isNullOrEmpty()) {
            context?.disableDefaultConstraintViolation()
            context?.buildConstraintViolationWithTemplate("비밀번호를 입력해주세요.")
                ?.addConstraintViolation()
            return false
        }

        if(!value.matches(Regex("^(?=.*[a-zA-Z])(?=.*[!@#\$%^+=-])(?=.*[0-9]).{8,16}\$"))) {
            context?.disableDefaultConstraintViolation()
            context?.buildConstraintViolationWithTemplate("비밀번호 형식이 맞지 않습니다.")
                ?.addConstraintViolation()
            return false
        }

        return true
    }
}
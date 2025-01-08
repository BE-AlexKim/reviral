package tech.server.reviral.common.config.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 *packageName    : tech.server.reviral.common.config.validation
 * fileName       : UsernameValidator
 * author         : joy58
 * date           : 2024-11-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-27        joy58       최초 생성
 */

class UsernameValidator: ConstraintValidator<Username, String>{

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {

        // 빈 문자열일 경우,
        if( value.isNullOrEmpty()) {
            context?.disableDefaultConstraintViolation()
            context?.buildConstraintViolationWithTemplate("아이디를 입력해 주세요.")
                ?.addConstraintViolation()
            return false
        }
        // 대문자가 포함되어 있을 경우
        if (value.contains(Regex("[A-Z]"))) {
            context?.disableDefaultConstraintViolation()
            context?.buildConstraintViolationWithTemplate("영문 대문자는 포함할 수 없습니다.")
                ?.addConstraintViolation()
            return false
        }

        // 아아디가 형식에 맞지 않을 경우.
//        if (!value.matches(Regex("^[a-zA-Z0-9]{4,16}$"))) {
//            context?.disableDefaultConstraintViolation()
//            context?.buildConstraintViolationWithTemplate("아이디는 영문 숫자 4-16자 사이로 입력해주세요.")
//                ?.addConstraintViolation()
//            return false
//        }

        return true
    }
}
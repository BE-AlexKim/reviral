package tech.server.reviral.api.account.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.Pattern
import tech.server.reviral.api.account.model.enums.Gender
import tech.server.reviral.config.validation.Credentials
import tech.server.reviral.config.validation.Username

/**
 *packageName    : tech.server.reviral.api.account.model.dto
 * fileName       : SignUpRequestDTO
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
@Schema(name = "사용자 회원가입 요청 객체")
data class SignUpRequestDTO(

    @Schema(description = "아이디")
    val loginId: String,

    @Schema(description = "비밀번호")
    val loginPw: String,

    @Schema(description = "이름")
    val username: String,

    @Enumerated(EnumType.STRING)
    @Schema(description = "성별")
    val gender: Gender,

    @Schema(description = "휴대폰 번호")
    val phoneNumber: String,

    @Schema(description = "주소")
    val address: String,

    @Schema(description = "N사 아이디", example = "dowpp32")
    val nvId: String? = null,

    @Schema(description = "C사 아이디", example = "eow32@gmail.com")
    val cpId: String? = null,

    @Schema(description = "이벤트 수신 동의", example = "true")
    val isEvent: Boolean = true

)

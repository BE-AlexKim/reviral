package tech.server.reviral.api.account.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.Pattern
import tech.server.reviral.api.account.model.enums.Gender
import tech.server.reviral.common.config.validation.Credentials
import tech.server.reviral.common.config.validation.Username

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
    @field:Username
    @Schema(description = "아이디")
    val loginId: String,

    @field:Credentials
    @Schema(description = "비밀번호")
    val loginPw: String,

    @field:Pattern(regexp = "^[가-힣]{2,4}$", message = "이름 형식이 맞지 않습니다.")
    @Schema(description = "이름")
    val username: String,

    @Enumerated(EnumType.STRING)
    @Schema(description = "성별")
    val gender: Gender,

    @field:Pattern(regexp = "^01[016789]\\\\d{7,8}\$", message = "휴대전화번호 형식이 맞지 않습니다.")
    @Schema(description = "휴대폰 번호")
    val phoneNumber: String,

    @Schema(description = "주소")
    val address: String,

    @Schema(description = "N사 아이디")
    val nId: String,

    @Schema(description = "C사 아이디")
    val cId: String


)

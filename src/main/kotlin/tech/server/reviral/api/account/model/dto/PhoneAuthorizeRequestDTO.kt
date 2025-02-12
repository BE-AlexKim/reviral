package tech.server.reviral.api.account.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern

/**
 *packageName    : tech.server.reviral.api.account.model.dto
 * fileName       : PhoneAuthorizeRequestDTO
 * author         : joy58
 * date           : 2025-02-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-08        joy58       최초 생성
 */
@Schema(name = "휴대폰 인증 요청 모델")
data class PhoneAuthorizeRequestDTO(
    @Schema(description = "휴대폰 번호(-없이)", example = "01012345678", required = true)
    val phone: String,
    @Schema(description = "인증번호(6자리)", example = "333444", required = false)
    val code: String? = null
)

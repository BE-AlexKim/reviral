package tech.server.reviral.api.account.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.account.model.dto
 * fileName       : AuthorizeCodeRequestDTO
 * author         : joy58
 * date           : 2025-01-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-06        joy58       최초 생성
 */
@Schema(name = "인증코드 확인 요청 모델")
data class AuthorizeCodeRequestDTO(
    @Schema(description = "사용자 이메일")
    val email: String,
    @Schema(description = "인증코드 값")
    val code: String,
)

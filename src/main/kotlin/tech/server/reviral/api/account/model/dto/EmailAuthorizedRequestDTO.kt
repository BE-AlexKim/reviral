package tech.server.reviral.api.account.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.account.model.dto
 * fileName       : EmailAuthorizedRequestDTO
 * author         : joy58
 * date           : 2025-01-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-06        joy58       최초 생성
 */
@Schema(name = "이메일 인증 요청 모델")
data class EmailAuthorizedRequestDTO(
    @Schema(description = "인증요청 메일주소")
    val email: String
)

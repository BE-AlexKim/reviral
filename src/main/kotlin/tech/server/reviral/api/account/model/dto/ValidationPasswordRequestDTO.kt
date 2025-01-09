package tech.server.reviral.api.account.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.account.model.dto
 * fileName       : ValidationPasswordRequestDTO
 * author         : joy58
 * date           : 2025-01-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-09        joy58       최초 생성
 */
@Schema(name = "비밀번호 검증 모델")
data class ValidationPasswordRequestDTO(
    @Schema(description = "사용자 일련번호")
    val userId: Long,
    @Schema(description = "검증할 비밀번호")
    val password: String? = null,
)

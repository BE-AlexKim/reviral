package tech.server.reviral.api.account.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.account.model.dto
 * fileName       : ReloadRequestDTO
 * author         : joy58
 * date           : 2024-11-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-28        joy58       최초 생성
 */
@Schema(name = "토큰 재발급 요청 모델")
data class ReloadRequestDTO(
    val accessToken: String,
    val refreshToken: String
)

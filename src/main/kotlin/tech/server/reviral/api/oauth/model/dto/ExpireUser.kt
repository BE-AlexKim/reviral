package tech.server.reviral.api.oauth.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.oauth.model.dto
 * fileName       : ExpireUser
 * author         : joy58
 * date           : 2025-02-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-08        joy58       최초 생성
 */
@Schema(name = "사용자 탈퇴 요청 모델")
data class ExpireUser(
    @Schema(description = "사용자 일련번호", required = true)
    val userId: Long
)

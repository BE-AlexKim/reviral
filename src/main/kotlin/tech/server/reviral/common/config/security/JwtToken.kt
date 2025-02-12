package tech.server.reviral.common.config.security

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.common.config.security
 * fileName       : JwtToken
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
@Schema(name = "JWT 토큰모델")
data class JwtToken(
    @Schema(description = "사용자 일련번호")
    val userId: Long,
    @Schema(description = "토큰 타입")
    val grantType: String = "Bearer",
    @Schema(description = "엑세스 토큰")
    val accessToken: String,
    @Schema(description = "리프레시 토큰")
    val refreshToken: String,
    @Schema(description = "사용자 정보 유무")
    var hasUserInfo: Boolean = false,
)

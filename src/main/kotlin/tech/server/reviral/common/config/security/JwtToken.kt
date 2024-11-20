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
    val grantType: String = "Bearer",
    val accessToken: String,
    val refreshToken: String
)

package tech.server.reviral.config.security

/**
 *packageName    : tech.server.reviral.common.config.security
 * fileName       : JwtClaims
 * author         : joy58
 * date           : 2025-01-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-07        joy58       최초 생성
 */
data class JwtClaims (
    val sub: String = "",
    val iss: String = "",
    val iat: Long = 0,
    val username: String = "",
    val roles: String = "",
    val exp: Long = 0
)
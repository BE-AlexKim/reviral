package tech.server.reviral.config.security

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key

/**
 *packageName    : tech.server.reviral.common.config.security
 * fileName       : JwtTokenHelper
 * author         : joy58
 * date           : 2024-11-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-21        joy58       최초 생성
 */
@Component
class JwtTokenHelper(
    @Value("\${spring.jwt.secret-key}")
    private var secretKey: String,
    @Value("\${spring.jwt.key-algorithms}")
    private var algorithm: SignatureAlgorithm,
    @Value("\${spring.jwt.expiration.access-time}")
    private var accessExpiredTime: Long,
    @Value("\${spring.jwt.expiration.refresh-time}")
    private var refreshExpiredTime: Long,
) {

    private val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

    /** Claim 정보 Decrypt
     * @param token
     * @return Claims
     */
    fun decryptClaims(token: String): Claims {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        }catch (e: ExpiredJwtException) {
            e.claims
        }
    }

}
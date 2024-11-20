package tech.server.reviral.common.config.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.Date

/**
 *packageName    : tech.server.reviral.common.config.security
 * fileName       : JwtTokenProvider
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
@Component
class JwtTokenProvider(
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

    fun getJwtToken(authentication: Authentication): JwtToken {
        val issuedAt = Date()
        val accessTokenExpiredDate = Date(issuedAt.time + accessExpiredTime )
        val refreshTokenExpiredDate = Date(issuedAt.time + refreshExpiredTime )

        val header = hashMapOf<String, Any>(
            "typ" to "JWT", "alg" to algorithm
        )

        val accessToken = Jwts.builder()
            .setHeader(header)
            .setSubject(authentication.name)
            .setIssuer("reviral.tech")
            .setIssuedAt(issuedAt)
            .claim("name",authentication.name)
            .setExpiration(accessTokenExpiredDate)
            .signWith(key, algorithm)
            .compact()

        val refreshToken = Jwts.builder()
            .setExpiration(refreshTokenExpiredDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()

        return JwtToken(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

}
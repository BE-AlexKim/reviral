package tech.server.reviral.common.config.security

import io.github.oshai.kotlinlogging.KotlinLogging
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.account.service.AccountService
import tech.server.reviral.common.config.response.exception.BasicException
import tech.server.reviral.common.config.response.exception.enums.BasicError
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.Date
import java.util.Optional

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

    private val customUserDetailsService: CustomUserDetailsService
) {

    private val log = KotlinLogging.logger {}
    private val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

    fun getJwtToken(user: User): JwtToken {
        val issuedAt = Date()
        val accessTokenExpiredDate = Date(issuedAt.time + accessExpiredTime )
        val refreshTokenExpiredDate = Date(issuedAt.time + refreshExpiredTime )

        val header = hashMapOf<String, Any>(
            "typ" to "JWT", "alg" to algorithm
        )

        val accessToken = Jwts.builder()
            .setHeader(header)
            .setSubject(user.id!!.toString())
            .setIssuer("RE:VIRAL.CO")
            .setIssuedAt(issuedAt)
            .claim("username",user.loginId)
            .claim("roles",user.auth)
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

    /**
     * Authorization이 Header에 토큰 존재 유무와 Grant Type 유효성 판단
     * @param request: HttpServletRequest
     * @return Optional<String>
     */
    fun extractToken(request: HttpServletRequest): Optional<String> {
        val authorizationToken = Optional.ofNullable(request.getHeader("Authorization"))
        return if ( !authorizationToken.isEmpty && authorizationToken.get().startsWith("Bearer")) {
            Optional.of(authorizationToken.get().substring(7))
        }else {
            Optional.empty<String>()
        }
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails = customUserDetailsService.loadUserByUsername(token)
        return UsernamePasswordAuthenticationToken(userDetails,userDetails?.password,userDetails?.authorities)
    }


    fun validateToken(request: HttpServletRequest, accessToken: String): Authentication {
        try {
            log.info { accessToken }
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken)
            return getAuthentication(accessToken)
        }catch (e: SignatureException) {
            log.warn { BasicError.INVALID_TOKEN }
            request.setAttribute("exception", BasicError.INVALID_TOKEN)
            throw BasicException(BasicError.INVALID_TOKEN)
        } catch (e: MalformedJwtException) {
            log.warn { BasicError.INVALID_TOKEN }
            request.setAttribute("exception", BasicError.INVALID_TOKEN)
            throw BasicException(BasicError.INVALID_TOKEN)
        } catch (e: ExpiredJwtException) {
            log.warn { BasicError.EXPIRED_ACCESS_TOKEN }
            request.setAttribute("exception", BasicError.EXPIRED_ACCESS_TOKEN)
            throw BasicException(BasicError.EXPIRED_ACCESS_TOKEN)
        } catch (e: UnsupportedJwtException) {
            log.warn { BasicError.AUTH_METHOD_UNSUPPORTED }
            request.setAttribute("exception", BasicError.AUTH_METHOD_UNSUPPORTED)
            throw BasicException(BasicError.AUTH_METHOD_UNSUPPORTED)
        } catch (e: IllegalArgumentException) {
            log.warn { BasicError.INVALID_TOKEN }
            request.setAttribute("exception", BasicError.INVALID_TOKEN)
            throw BasicException(BasicError.INVALID_TOKEN)
        }
    }

}
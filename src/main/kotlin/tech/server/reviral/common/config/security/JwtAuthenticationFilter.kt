package tech.server.reviral.common.config.security

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Optional

/**
 *packageName    : tech.server.reviral.common.config.security
 * fileName       : JwtAuthenticationFilter
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
): OncePerRequestFilter() {

    private val log = KotlinLogging.logger{}

    override fun doFilterInternal( request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain ) {
        val token = jwtTokenProvider.extractToken(request)
        log.debug { "token ::::: $token" }
        if ( token.isEmpty ) {
            filterChain.doFilter(request, response)
            return
        }else {
            val enableAccessToken = jwtTokenProvider.validateToken(request, token.get())
            SecurityContextHolder.getContext().authentication = enableAccessToken
            filterChain.doFilter(request, response)
        }
    }
}
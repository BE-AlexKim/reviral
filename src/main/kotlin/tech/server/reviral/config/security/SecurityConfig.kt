package tech.server.reviral.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

/**
 *packageName    : tech.server.reviral.common.config.security
 * fileName       : SeucirityConfig
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Value("\${spring.http.cors.allowed-origins}")
    private lateinit var origins: List<String>

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http.httpBasic { it.disable() }
        http.csrf { it.disable() }
        http.formLogin { it. disable() }
        http.cors { it.disable() }
        http.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

        http.authorizeHttpRequests {
            // Campaign URL Permit
            it.requestMatchers(
                "/api/v1/business/campaigns/{campaignId}",
                "/api/v1/users/id-check",
                "/api/v1/campaign/{campaignDetailsId}",
            ).permitAll()
            // User Authorized
            it.requestMatchers(
                HttpMethod.POST,
                "/api/v1/business/signup",
                "/api/v1/business/sign-in",
                "/api/v1/oauth/callback/kakao",
                "/api/v1/oauth/callback/naver",
                "/api/v1/users/phone/authorize/valid",
                "/api/v1/users/phone/authorize/send",
                "/api/v1/campaign/save",
                "/api/v1/users/email/verify/{type}",
                "/api/v1/users/email/authorized/{type}",
                "/api/v1/users/admin/sign-in",
                "/api/v1/users/sign-in",
                "/api/v1/users/sign-up/**",
                "/api/v1/users/logout/{userId}",
                "/api/v1/users/reload",
                "/api/v1/business/campaign/enroll/check/{imageCheckStatus}",
                "/api/v1/business/campaign/enroll/start",
                "/api/v1/business/campaign/detail",
                "/api/v1/business/campaign/begin",
                "/api/v1/business/campaign/begin/separate",
            ).permitAll()

            it.requestMatchers(
                HttpMethod.GET,
                "/api/v1/users/redirect",
                "/api/v1/campaign",
                "/api/v1/oauth/authorize/{provider}"
            ).permitAll()

            it.requestMatchers(
                HttpMethod.PUT, "/api/v1/campaign/{campaignId}"
            ).permitAll()

            it.requestMatchers(
                HttpMethod.GET,
                "/api/v1/business/reviewers",
                "/api/v1/business/campaigns",
                "/api/v1/business/campaigns"
            ).hasAuthority("ROLE_ADMIN")

            it.requestMatchers(
                HttpMethod.POST,
                "/api/v1/business/campaign/add",
                "/api/v1/business/campaign/add"
            ).hasAuthority("ROLE_ADMIN")

            it.requestMatchers(
                HttpMethod.DELETE,
                "/api/v1/business/campaign/add"
            ).hasAuthority("ROLE_ADMIN")

            it.requestMatchers(
                HttpMethod.GET,
                "/","/campaign/register","user","/campaign"
            ).permitAll()

            // Resource And OAuth Permit
            it.requestMatchers(
                "/configuration/**",
                "/v2/api-docs/**",
                "/v2/api-docs",
                "/swagger-ui/**/*",
                "/webjars/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/static/images/**",
                "/static/css"
            ).permitAll()

            it.anyRequest().authenticated()
        }

        http.exceptionHandling {
            it.authenticationEntryPoint(CustomAuthenticationEntryPoint())
            it.accessDeniedHandler(CustomAccessDeniedHandler())
        }

        http.addFilterBefore(
            JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter::class.java
        )

        http.addFilterBefore(
            corsFilter(),
            JwtAuthenticationFilter(jwtTokenProvider)::class.java
        )

        return http.build()
    }

    @Bean(name = ["CorsFilter"])
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun corsFilter(): CorsFilter {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.allowedOrigins = origins
        config.allowedMethods = listOf("*")
        config.allowedHeaders = listOf("*")
        config.exposedHeaders = listOf("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**",config)

        return CorsFilter(source)
    }
}
package tech.server.reviral.common.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
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
                HttpMethod.GET,
                "/api/v1/campaign",
                "/api/v1/campaign/{campaignDetailsId}",
                "/api/v1/users/id-check",
            ).permitAll()

            it.requestMatchers(
                HttpMethod.POST,"/api/v1/campaign/save",
                "/api/v1/users/email/verify/{type}",
                "/api/v1/users/email/authorized/{type}",
                "/api/v1/users/admin/sign-in",
                "/api/v1/users/sign-in",
                "/api/v1/users/sign-up",
                "/api/v1/users/reload"
            ).permitAll()

            it.requestMatchers(
                HttpMethod.PUT, "/api/v1/campaign/{campaignId}"
            ).permitAll()

            it.requestMatchers(
                HttpMethod.POST,
                "/api/v1/campaign/admin/list/{campaignId}",
                "/api/v1/campaign/admin/list",
            ).hasAuthority("ROLE_ADMIN")

            // Resource Permit
            it.requestMatchers(
                "/configuration/**",
                "/v2/api-docs/**",
                "/v2/api-docs",
                "/swagger-ui/**/*",
                "/webjars/**",
                "/v3/api-docs/**",
                "/swagger-resources/**"
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
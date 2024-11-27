package tech.server.reviral.common.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

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
            // URL Permit
            it.requestMatchers(
                "/api/v1/users/sign-in",
                "/api/v1/users/sign-up"
            ).permitAll()
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

        return http.build()
    }
}
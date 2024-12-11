package tech.server.reviral.common.config.docs

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.hibernate.internal.util.collections.CollectionHelper.listOf
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

/**
 *packageName    : tech.server.reviral.common.config
 * fileName       : SwaggerConfig
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@Configuration
class SwaggerConfig {

    private fun apiInfo(): Info {
        return Info()
            .title("개발자 문서")
            .description("<h1>리바이럴(REVIRAL)</h2> <hr> <h3>리바이럴 백엔드 API 연동 문서</h2>")
            .version("v1.0.0")
    }

    @Bean
    fun securityComponents(): Components {
        return Components().addSecuritySchemes("auth",
            SecurityScheme()
                .name(HttpHeaders.AUTHORIZATION)
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .`in`(SecurityScheme.In.HEADER)
                .bearerFormat("Authorization")
        )
    }

    @Bean
    fun openApi(): OpenAPI {
        val securityRequirement = SecurityRequirement()
            .addList("auth", listOf("read","write"))

        return OpenAPI()
            .addServersItem(Server().url("http://localhost:9093"))
            .addServersItem(Server().url("http://api-server.reviral.kr"))
            .addServersItem(Server().url("http://172.30.1.113:9093"))
            .components(securityComponents())
            .addSecurityItem(securityRequirement)
            .info(apiInfo())
    }



}
package tech.server.reviral.config.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.json.simple.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import tech.server.reviral.config.response.exception.ExceptionMessageInitializer
import tech.server.reviral.config.response.exception.enums.BasicError
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 *packageName    : tech.server.reviral.common.config.security
 * fileName       : AuthenticationEntryPoint
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
@Component
class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {

    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: AuthenticationException?) {
        val error = request?.getAttribute("exception") as BasicError?
            ?: BasicError.UNAUTHORIZED

        sendError(error, response)

    }

    private fun sendError(error: ExceptionMessageInitializer, response: HttpServletResponse?) {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        val json = JSONObject()
        json["code"] = error.getCode()
        json["error"] = error.getError()
        json["message"] = error.getMessage()
        json["timestamp"] = timestamp

        response?.status = error.getHttpStatus().value()
        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.characterEncoding = Charsets.UTF_8.toString()
        response?.writer?.print(json)
    }

}
package tech.server.reviral.common.config.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.json.simple.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import tech.server.reviral.common.config.response.exception.enums.BasicError
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 *packageName    : tech.server.reviral.common.config.security
 * fileName       : CustomAccessDeniedHandler
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
@Component
class CustomAccessDeniedHandler: AccessDeniedHandler {

    override fun handle(request: HttpServletRequest?, response: HttpServletResponse?, accessDeniedException: AccessDeniedException?) {

        val authError = BasicError.AUTH_IS_NOT_EMPTY

        val json = JSONObject()
        json["status"] = authError.getHttpStatus().value()
        json["code"] = authError.getCode()
        json["message"] = authError.getMessage()
        json["timestamp"] = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

        response?.status = HttpStatus.UNAUTHORIZED.value()
        response?.contentType = MediaType.APPLICATION_JSON_VALUE
        response?.characterEncoding = Charsets.UTF_8.toString()
        response?.writer?.print(json)

    }
}
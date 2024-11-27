package tech.server.reviral.common.config.response.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 *packageName    : tech.server.reviral.common.config.response.exception
 * fileName       : CustomExceptionHandler
 * author         : joy58
 * date           : 2024-11-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-27        joy58       최초 생성
 */
@RestControllerAdvice
class CustomExceptionHandler: ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {

        val message = ExceptionDTO(
            status = status.value(),
            code = "HV000033",
            message = ex.bindingResult.fieldError?.defaultMessage
        )

        return ResponseEntity(message, headers, status)
    }

    @ExceptionHandler(BasicException::class)
    fun handlerBasicException(ex: BasicException): ResponseEntity<ExceptionDTO> {
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
            .body(
                ExceptionDTO(
                    status = ex.getErrorCode().getHttpStatus().value(),
                    code = ex.getErrorCode().getCode(),
                    message = ex.getErrorCode().getMessage()
                )
            )
    }

}
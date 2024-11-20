package tech.server.reviral.common.config.response.exception

import org.springframework.http.HttpStatus
import tech.server.reviral.common.config.response.exception.enums.BasicError
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 *packageName    : tech.server.reviral.common.config.response.exception
 * fileName       : DefaultExceptionResolver
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
open class DefaultExceptionResolver: RuntimeException {

    private var exceptionMessageInitializer: ExceptionMessageInitializer

    private object DefaultErrorCodeHolder {
        val DEFAULT_ERROR_CODE = object : ExceptionMessageInitializer {

            override fun getCode(): String {
                return BasicError.DEFAULT.getCode()
            }

            override fun getError(): String {
                return BasicError.DEFAULT.getError()
            }

            override fun getHttpStatus(): HttpStatus {
                return BasicError.DEFAULT.getHttpStatus()
            }

            override fun getMessage(): String {
                return BasicError.DEFAULT.getMessage()
            }

            override fun defaultException(): RuntimeException {
                return DefaultExceptionResolver(BasicError.DEFAULT)
            }

            override fun setException(cause: Throwable): RuntimeException {
                return DefaultExceptionResolver(BasicError.DEFAULT, cause)
            }

            override fun setTimestamp(): String {
                return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            }
        }
    }

    constructor(message: String) : super(message) {
        exceptionMessageInitializer = getDefaultErrorCode()
    }

    constructor(message: String, cause: Throwable) : super(message, cause) {
        exceptionMessageInitializer = getDefaultErrorCode()
    }

    constructor(exceptionCode: ExceptionMessageInitializer) : super(exceptionCode.getMessage()) {
        this.exceptionMessageInitializer = exceptionCode
    }

    constructor(exceptionCode: ExceptionMessageInitializer, cause: Throwable) : super(exceptionCode.getMessage(), cause) {
        this.exceptionMessageInitializer = exceptionCode
    }

    open fun getErrorCode(): ExceptionMessageInitializer {
        return exceptionMessageInitializer
    }

    private fun getDefaultErrorCode(): ExceptionMessageInitializer {
        return DefaultErrorCodeHolder.DEFAULT_ERROR_CODE
    }
}

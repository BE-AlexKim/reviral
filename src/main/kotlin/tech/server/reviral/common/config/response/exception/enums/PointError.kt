package tech.server.reviral.common.config.response.exception.enums

import org.springframework.http.HttpStatus
import tech.server.reviral.common.config.response.exception.BasicException
import tech.server.reviral.common.config.response.exception.ExceptionMessageInitializer
import tech.server.reviral.common.config.response.exception.PointException
import tech.server.reviral.common.config.response.exception.message.PointExceptionMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 *packageName    : tech.server.reviral.common.config.response.exception.enums
 * fileName       : PointError
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
enum class PointError(
    private val status: HttpStatus,
    private val code: String,
    private val message: String
): ExceptionMessageInitializer {

    EXCHANGE_BETTER_THAN_REMAIN(HttpStatus.BAD_REQUEST, "PE0002", PointExceptionMessage.EXCHANGE_BETTER_THAN_REMAIN),
    POINT_IS_NOT_EXIST(HttpStatus.BAD_REQUEST, "PE0001", PointExceptionMessage.POINT_IS_NOT_EXIST);

    override fun getCode(): String {
        return code
    }

    override fun getError(): String {
        return name
    }

    override fun getMessage(): String {
        return message
    }

    override fun getHttpStatus(): HttpStatus {
        return status
    }

    override fun defaultException(): PointException {
        return PointException(this)
    }

    override fun setException(cause: Throwable): PointException {
        return PointException(this, cause)
    }

    override fun setTimestamp(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
}
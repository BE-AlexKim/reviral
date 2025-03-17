package tech.server.reviral.config.response.exception

import org.springframework.http.HttpStatus

/**
 *packageName    : tech.server.reviral.common.config.response.exception
 * fileName       : ExceptionMessageInitializer
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
interface ExceptionMessageInitializer {

    fun getCode(): String

    fun getError(): String

    fun getHttpStatus(): HttpStatus

    fun getMessage(): String

    fun defaultException(): RuntimeException

    fun setException(cause: Throwable): RuntimeException

    fun setTimestamp(): String

}
package tech.server.reviral.config.response.exception

/**
 *packageName    : tech.server.reviral.common.config.response.exception.message
 * fileName       : PointException
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
class PointException: DefaultExceptionResolver {

    constructor(message: String): super(message)

    constructor(message: String, cause: Throwable): super(message, cause)

    constructor(exceptionCode: ExceptionMessageInitializer): super(exceptionCode)

    constructor(exceptionCode: ExceptionMessageInitializer, cause: Throwable): super(exceptionCode, cause)
}
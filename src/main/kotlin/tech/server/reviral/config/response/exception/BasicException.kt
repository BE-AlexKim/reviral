package tech.server.reviral.config.response.exception

/**
 *packageName    : tech.server.reviral.common.config.response.exception
 * fileName       : BasicException
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
class BasicException: DefaultExceptionResolver {

    constructor(message: String): super(message)

    constructor(message: String, cause: Throwable): super(message, cause)

    constructor(exceptionCode: ExceptionMessageInitializer): super(exceptionCode)

    constructor(exceptionCode: ExceptionMessageInitializer, cause: Throwable): super(exceptionCode, cause)

}
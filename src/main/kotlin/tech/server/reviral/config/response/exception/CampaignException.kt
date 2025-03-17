package tech.server.reviral.config.response.exception

/**
 *packageName    : tech.server.reviral.common.config.response.exception.message
 * fileName       : CampaignException
 * author         : joy58
 * date           : 2024-12-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-03        joy58       최초 생성
 */
class CampaignException: DefaultExceptionResolver {

    constructor(message: String): super(message)

    constructor(message: String, cause: Throwable): super(message, cause)

    constructor(exceptionCode: ExceptionMessageInitializer): super(exceptionCode)

    constructor(exceptionCode: ExceptionMessageInitializer, cause: Throwable): super(exceptionCode, cause)

}
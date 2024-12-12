package tech.server.reviral.common.config.response.exception.enums

import org.springframework.http.HttpStatus
import tech.server.reviral.common.config.response.exception.BasicException
import tech.server.reviral.common.config.response.exception.CampaignException
import tech.server.reviral.common.config.response.exception.ExceptionMessageInitializer
import tech.server.reviral.common.config.response.exception.message.BasicExceptionMessage
import tech.server.reviral.common.config.response.exception.message.CampaignExceptionMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 *packageName    : tech.server.reviral.common.config.response.exception.enums
 * fileName       : CampaignError
 * author         : joy58
 * date           : 2024-12-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-03        joy58       최초 생성
 */
enum class CampaignError(
    private val status: HttpStatus,
    private val code: String,
    private val message: String
): ExceptionMessageInitializer {
    CAMPAIGN_IS_NOT_EXIST(HttpStatus.BAD_REQUEST, "CP0004", CampaignExceptionMessage.CAMPAIGN_IS_NOT_EXIST),
    OPTION_IS_NOT_EMPTY(HttpStatus.BAD_REQUEST,"CP0003", CampaignExceptionMessage.OPTION_IS_NOT_EMPTY),
    START_DATE_SET(HttpStatus.BAD_REQUEST, "CP0002", CampaignExceptionMessage.START_DATE_SET),
    DEFAULT(HttpStatus.INTERNAL_SERVER_ERROR, "CP0001", CampaignExceptionMessage.DEFAULT);

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

    override fun defaultException(): CampaignException {
        return CampaignException(this)
    }

    override fun setException(cause: Throwable): CampaignException {
        return CampaignException(this, cause)
    }

    override fun setTimestamp(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
}
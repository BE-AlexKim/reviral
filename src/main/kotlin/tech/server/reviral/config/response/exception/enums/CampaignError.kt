package tech.server.reviral.config.response.exception.enums

import org.springframework.http.HttpStatus
import tech.server.reviral.config.response.exception.CampaignException
import tech.server.reviral.config.response.exception.ExceptionMessageInitializer
import tech.server.reviral.config.response.exception.message.CampaignExceptionMessage
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
    CAMPAIGN_EXIST_JOIN_PEOPLE(HttpStatus.BAD_REQUEST,"CP0024",CampaignExceptionMessage.CAMPAIGN_EXIST_JOIN_PEOPLE),
    CAMPAIGN_DO_NOT_DELETE(HttpStatus.BAD_REQUEST,"CP0023", CampaignExceptionMessage.CAMPAIGN_DO_NOT_DELETE),
    CAMPAIGN_STATUS_PROGRESS(HttpStatus.BAD_REQUEST, "CP0022", CampaignExceptionMessage.CAMPAIGN_STATUS_PROGRESS),
    CAMPAIGN_MODIFY_IMPOSSIBLE(HttpStatus.BAD_REQUEST, "CP0021", CampaignExceptionMessage.CAMPAIGN_MODIFY_IMPOSSIBLE),
    CAMPAIGN_ENROLL_NOT_MATCH(HttpStatus.BAD_REQUEST, "CP0020", CampaignExceptionMessage.CAMPAIGN_ENROLL_NOT_MATCH),
    CAMPAIGN_STATUS_NOT_WAIT(HttpStatus.BAD_REQUEST,"CP0019", CampaignExceptionMessage.CAMPAIGN_STATUS_NOT_WAIT),
    CAMPAIGN_STATUS_WAIT(HttpStatus.BAD_REQUEST, "CP0018", CampaignExceptionMessage.CAMPAIGN_STATUS_WAIT),
    CAMPAIGN_CP_ID_NULL(HttpStatus.BAD_REQUEST,"CP0017", CampaignExceptionMessage.CAMPAIGN_CP_ID_NULL),
    CAMPAIGN_NV_ID_NULL(HttpStatus.BAD_REQUEST, "CP0016", CampaignExceptionMessage.CAMPAIGN_NV_ID_NULL),
    CAMPAIGN_CANCEL_JOIN(HttpStatus.BAD_REQUEST,"CP0015", CampaignExceptionMessage.CAMPAIGN_CANCEL_JOIN),
    CAMPAIGN_IS_NOT_DUPLICATED(HttpStatus.BAD_REQUEST,"CP0014", CampaignExceptionMessage.CAMPAIGN_IS_NOT_DUPLICATED),
    CAMPAIGN_START_NOT_YET(HttpStatus.BAD_REQUEST,"CP0013",CampaignExceptionMessage.CAMPAIGN_START_NOT_YET),
    CAMPAIGN_FULL_RECRUIT(HttpStatus.BAD_REQUEST, "CP0012", CampaignExceptionMessage.CAMPAIGN_FULL_RECRUIT),
    CAMPAIGN_DO_NOT_CANCEL(HttpStatus.BAD_REQUEST, "CP0011", CampaignExceptionMessage.CAMPAIGN_DO_NOT_CANCEL),
    CAMPAIGN_REVIEW(HttpStatus.BAD_REQUEST, "CP0010", CampaignExceptionMessage.CAMPAIGN_REVIEW),
    CAMPAIGN_ORDER_NO(HttpStatus.BAD_REQUEST, "CP0009", CampaignExceptionMessage.CAMPAIGN_ORDER_NO),
    REGISTER_CAMPAIGN_NOT_EXIST(HttpStatus.BAD_REQUEST, "CP0008", CampaignExceptionMessage.REGISTER_CAMPAIGN_NOT_EXIST),
    SUB_OPTION_IS_NOT_CONTAIN_OPTION(HttpStatus.BAD_REQUEST,"CP0007", CampaignExceptionMessage.SUB_OPTION_IS_NOT_CONTAIN_OPTION),
    SUB_OPTION_IS_NOT_EMPTY(HttpStatus.BAD_REQUEST, "CP0006", CampaignExceptionMessage.SUB_OPTION_IS_NOT_EMPTY),
    CAMPAIGN_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "CP0005", CampaignExceptionMessage.CAMPAIGN_NOT_COMPLETED),
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
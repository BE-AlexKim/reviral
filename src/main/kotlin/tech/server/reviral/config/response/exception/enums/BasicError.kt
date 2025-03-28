package tech.server.reviral.config.response.exception.enums

import org.springframework.http.HttpStatus
import tech.server.reviral.config.response.exception.BasicException
import tech.server.reviral.config.response.exception.ExceptionMessageInitializer
import tech.server.reviral.config.response.exception.message.BasicExceptionMessage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 *packageName    : tech.server.reviral.common.config.response.exception.enums
 * fileName       : BasicError
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
enum class BasicError(
    private val status: HttpStatus,
    private val code: String,
    private val message: String
): ExceptionMessageInitializer {

    DEFAULT( HttpStatus.INTERNAL_SERVER_ERROR,"ME0001", BasicExceptionMessage.DEFAULT),
    HEADER_TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "BE0001", BasicExceptionMessage.HEADER_TOKEN_NOT_EXIST),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "BE0002", BasicExceptionMessage.INVALID_TOKEN),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "BE0003", BasicExceptionMessage.EXPIRED_ACCESS_TOKEN),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "BE0004", BasicExceptionMessage.EXPIRED_REFRESH_TOKEN),
    AUTH_METHOD_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "BE0005", BasicExceptionMessage.AUTH_METHOD_UNSUPPORTED ),
    AUTH_IS_NOT_EMPTY(HttpStatus.NOT_ACCEPTABLE, "BE0006", BasicExceptionMessage.AUTH_IS_NOT_EMPTY),
    TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "BE0007", BasicExceptionMessage.TOKEN_ERROR),
    REFRESH_TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED,"BE0008", BasicExceptionMessage.REFRESH_TOKEN_NOT_EXIST),
    TOKEN_NOT_MATCH(HttpStatus.UNAUTHORIZED, "BE0009", BasicExceptionMessage.TOKEN_NOT_MATCH),
    USER_NOT_MATCH(HttpStatus.BAD_REQUEST, "BE0010", BasicExceptionMessage.USER_NOT_MATCH),
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST,"BE0011", BasicExceptionMessage.USER_NOT_EXIST),
    USERNAME_DUPLICATED(HttpStatus.BAD_REQUEST,"BE0012", BasicExceptionMessage.USERNAME_DUPLICATED),
    USER_CREDENTIALS_NOT_MATCH(HttpStatus.BAD_REQUEST, "BE0013", BasicExceptionMessage.USER_CREDENTIALS_NOT_MATCH),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST,"BE0014",BasicExceptionMessage.USER_ALREADY_EXIST),
    AUTHORIZED_EMAIL(HttpStatus.BAD_REQUEST, "BE0015", BasicExceptionMessage.AUTHORIZED_EMAIL),
    AUTHORIZED_CODE_NOT_MATCH(HttpStatus.BAD_REQUEST, "BE0016", BasicExceptionMessage.AUTHORIZED_CODE_NOT_MATCH),
    FILE_IMG_EXTENSION(HttpStatus.BAD_REQUEST, "BE0017", BasicExceptionMessage.FILE_IMG_EXTENSION),
    FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "BE0018", BasicExceptionMessage.FILE_UPLOAD_FAIL),
    SECONDARY_PASSWORD_SET(HttpStatus.BAD_REQUEST, "BE0019", BasicExceptionMessage.SECONDARY_PASSWORD_SET),
    UNSUPPORTED_URL(HttpStatus.NOT_FOUND,"BE0020", BasicExceptionMessage.UNSUPPORTED_URL),
    AUTH_NOT_ADMIN(HttpStatus.BAD_REQUEST,"BE0021",BasicExceptionMessage.AUTH_NOT_ADMIN),
    UNSUPPORTED_REGISTRATION(HttpStatus.INTERNAL_SERVER_ERROR, "BE0022", BasicExceptionMessage.UNSUPPORTED_REGISTRATION),
    SID_NOT_EXIST(HttpStatus.BAD_REQUEST, "BE0023", BasicExceptionMessage.SID_NOT_EXIST),
    BLACK_LIST_USER(HttpStatus.UNAUTHORIZED, "BE0024", BasicExceptionMessage.BLACK_LIST_USER),
    OAUTH_CLIENT_ERROR(HttpStatus.BAD_REQUEST, "BE0025", BasicExceptionMessage.OAUTH_CLIENT_ERROR),
    SERVER_NETWORK_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"BE0026",BasicExceptionMessage.SERVER_NETWORK_ERROR),
    RESPONSE_TYPE_ERROR(HttpStatus.BAD_REQUEST,"BE0026",BasicExceptionMessage.RESPONSE_TYPE_ERROR),
    AUTHORIZED_CODE_EXPIRED(HttpStatus.BAD_REQUEST,"BE0027", BasicExceptionMessage.AUTHORIZED_CODE_EXPIRED),
    FILE_SIZE_FULL(HttpStatus.BAD_REQUEST,"BE0028",BasicExceptionMessage.FILE_SIZE_FULL),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "E00002", BasicExceptionMessage.UNAUTHORIZED);


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

    override fun defaultException(): BasicException {
        return BasicException(this)
    }

    override fun setException(cause: Throwable): BasicException {
        return BasicException(this, cause)
    }

    override fun setTimestamp(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

}
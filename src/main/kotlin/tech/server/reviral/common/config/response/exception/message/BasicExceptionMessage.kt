package tech.server.reviral.common.config.response.exception.message

/**
 *packageName    : tech.server.reviral.common.config.response.exception.message
 * fileName       : BasicExceptionMessage
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
object BasicExceptionMessage {

    const val HEADER_TOKEN_NOT_EXIST = ""
    const val INVALID_TOKEN = ""
    const val EXPIRED_ACCESS_TOKEN = ""
    const val EXPIRED_REFRESH_TOKEN = ""
    const val AUTH_METHOD_UNSUPPORTED = ""
    const val AUTH_IS_NOT_EMPTY = ""
    const val TOKEN_ERROR = ""
    const val REFRESH_TOKEN_NOT_EXIST = ""
    const val TOKEN_NOT_MATCH = ""
    const val USER_NOT_MATCH = "사용자가 일치하지 않습니다."
    const val UNAUTHORIZED = "인증되지 않은 사용자입니다."
    const val DEFAULT = "서버 오류"
    const val USER_NOT_EXIST = "사용자가 존재하지 않습니다."

}
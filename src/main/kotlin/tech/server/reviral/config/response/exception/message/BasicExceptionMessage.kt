package tech.server.reviral.config.response.exception.message

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

    const val HEADER_TOKEN_NOT_EXIST = "토큰이 존재하지 않습니다."
    const val INVALID_TOKEN = "유효하지 않는 토큰정보입니다."
    const val EXPIRED_ACCESS_TOKEN = "인증이 만료되었습니다."
    const val EXPIRED_REFRESH_TOKEN = "재사용 토큰이 만료되었습니다."
    const val AUTH_METHOD_UNSUPPORTED = "지원하지 않는 접근 권한입니다."
    const val AUTH_IS_NOT_EMPTY = "접근 권한이 없습니다."
    const val TOKEN_ERROR = "토큰이 정상적으로 작동하지 않습니다."
    const val REFRESH_TOKEN_NOT_EXIST = "만료된 접근입니다."
    const val TOKEN_NOT_MATCH = "잘못된 접근입니다."
    const val USER_NOT_MATCH = "사용자가 일치하지 않습니다."
    const val UNAUTHORIZED = "인증되지 않은 사용자입니다."
    const val DEFAULT = "서버 오류"
    const val USER_NOT_EXIST = "사용자가 존재하지 않습니다."
    const val USERNAME_DUPLICATED = "이미 존재하는 아이디입니다."
    const val USER_CREDENTIALS_NOT_MATCH = "비밀번호가 일치하지 않습니다."
    const val USER_ALREADY_EXIST = "이미 사용중인 계정입니다."
    const val AUTHORIZED_EMAIL = "인증코드를 재발송하여 다시 시도해주세요."
    const val AUTHORIZED_CODE_NOT_MATCH = "인증코드가 일치하지 않습니다."
    const val UNSUPPORTED_URL = "지원하지 않는 주소입니다."
    const val FILE_IMG_EXTENSION = "지원하지 않는 확장자입니다."
    const val FILE_UPLOAD_FAIL = "파일 업로드에 실패하였습니다."
    const val SECONDARY_PASSWORD_SET = "2차 비밀번호를 설정해주세요."
    const val AUTH_NOT_ADMIN = "관리자 계정 권한이 없습니다."
    const val UNSUPPORTED_REGISTRATION = "지원하지 않는 소셜 제공업체입니다."
    const val SID_NOT_EXIST = "소셜 제공 아이디가 존재하지 않습니다."
    const val BLACK_LIST_USER = "사용이 제재된 회원입니다."
    const val OAUTH_CLIENT_ERROR = "잘못된 OAUTH 요청입니다"
    const val SERVER_NETWORK_ERROR = "네트워크 연결 지연"
    const val RESPONSE_TYPE_ERROR = "응답 타입이 일치하지 않습니다."
    const val AUTHORIZED_CODE_EXPIRED = "인증코드가 만료되었습니다."
    const val FILE_SIZE_FULL = "이미지 파일 사이즈가 너무 큽니다"
}
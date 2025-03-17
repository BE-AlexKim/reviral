package tech.server.reviral.config.response.exception.message

/**
 *packageName    : tech.server.reviral.common.config.response.exception.message
 * fileName       : PointExceptionMessage
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
object PointExceptionMessage {
    const val POINT_PW_IS_NULL = "내 계정 < 포인트 전환 비밀번호에서 비밀번호를 등록해주세요."
    const val ACCOUNT_IS_NULL = "내 계정 < 계좌번호에서 계좌번호를 등록해주세요."
    const val EXCHANGE_BETTER_THAN_REMAIN = "전환 가능한 포인트 액수를 초과했습니다."
    const val POINT_IS_NOT_EXIST = "포인트 정보가 존재하지 않습니다."
}
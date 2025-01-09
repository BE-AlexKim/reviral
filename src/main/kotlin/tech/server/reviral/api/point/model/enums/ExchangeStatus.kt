package tech.server.reviral.api.point.model.enums

/**
 *packageName    : tech.server.reviral.api.point.model.enums
 * fileName       : ExchangeStatus
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
enum class ExchangeStatus(private val desc: String) {
    REQ("포인트 전환 요청"),
    FINISH("포인트 전환 완료"),
    CANCEL("포인트 전환 취소"),
    REJECT("포인트 전환 거절"),
    FAIL("포인트 전환 실패");

    fun description(point: Int): String {
        return "$point $desc"
    }
}
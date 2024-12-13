package tech.server.reviral.api.point.model.enums

/**
 *packageName    : tech.server.reviral.api.point.model.enums
 * fileName       : PointStatus
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
enum class PointStatus(private val desc: String) {

    EXPECT("적립예정"),
    CHANGE("전환가능"),
    COMPLETE("전환완료"),
    CANCEL("캠페인 취소");

    fun description() = desc
}
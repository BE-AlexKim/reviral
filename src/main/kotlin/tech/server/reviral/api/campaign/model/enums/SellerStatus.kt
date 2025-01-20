package tech.server.reviral.api.campaign.model.enums

/**
 *packageName    : tech.server.reviral.api.campaign.model.enums
 * fileName       : SellerStatus
 * author         : joy58
 * date           : 2025-01-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-13        joy58       최초 생성
 */
enum class SellerStatus(private val desc: String) {
    ACTIVE("활성화"),
    WAIT("대기중"),
    COMPLETE("완료");

    fun description() = desc
}
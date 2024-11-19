package tech.server.reviral.api.campaign.model.enums

/**
 *packageName    : tech.server.reviral.api.campaign.model.enums
 * fileName       : CampaignStatus
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
enum class CampaignStatus(private val desc: String) {
    WAIT("대기중"),RECRUITMENT("모집중"),PROGRESS("진행중"),FINISH("마감");

}
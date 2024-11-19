package tech.server.reviral.api.campaign.model.enums

/**
 *packageName    : tech.server.reviral.api.campaign.model.enums
 * fileName       : CampaignCategory
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
enum class CampaignCategory(private val desc: String) {
    TIME("시간구매"),DAILY("당일구매");
}
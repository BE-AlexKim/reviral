package tech.server.reviral.api.campaign.model.enums

/**
 *packageName    : tech.server.reviral.api.campaign.model.enums
 * fileName       : CampaignPlatform
 * author         : joy58
 * date           : 2024-12-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-03        joy58       최초 생성
 */
enum class CampaignPlatform(private val desc: String) {
    NAVER("네이버"), COUPANG("쿠팡"), ETC("기타")
}
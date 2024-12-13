package tech.server.reviral.api.campaign.model.entity.pk

import java.io.Serializable


/**
 *packageName    : tech.server.reviral.api.campaign.model.entity.pk
 * fileName       : CampaignEnrollId
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
data class CampaignEnrollId (
    val id: Long? = null,
    val enrollCount: Int = 0
): Serializable

package tech.server.reviral.api.campaign.model.entity.pk

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
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
@Embeddable
data class CampaignEnrollId (
    @Column(name = "campaign_enroll_id")
    val id: Long? = null,
    @Column(name = "campaign_enroll_count")
    val enrollCount: Int = 0
): Serializable

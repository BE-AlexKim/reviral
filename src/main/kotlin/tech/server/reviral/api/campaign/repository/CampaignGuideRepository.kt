package tech.server.reviral.api.campaign.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.server.reviral.api.campaign.model.entity.Campaign
import tech.server.reviral.api.campaign.model.entity.CampaignGuide

/**
 *packageName    : tech.server.reviral.api.campaign.repository
 * fileName       : CampaignGuideRepository
 * author         : joy58
 * date           : 2025-02-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-12        joy58       최초 생성
 */
@Repository
interface CampaignGuideRepository: JpaRepository<CampaignGuide, Long> {

    fun findByCampaign(campaign: Campaign): CampaignGuide
}
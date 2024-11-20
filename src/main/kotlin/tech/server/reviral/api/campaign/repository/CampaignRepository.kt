package tech.server.reviral.api.campaign.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.server.reviral.api.campaign.model.entity.Campaign

/**
 * packageName    : tech.server.reviral.api.campaign.repository
 * fileName       : CampaignRepository
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@Repository
interface CampaignRepository: JpaRepository<Campaign, Long> {

}
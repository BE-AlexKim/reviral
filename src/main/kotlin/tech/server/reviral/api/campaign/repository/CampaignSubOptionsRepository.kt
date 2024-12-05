package tech.server.reviral.api.campaign.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.server.reviral.api.campaign.model.entity.CampaignSubOptions

/**
 *packageName    : tech.server.reviral.api.campaign.repository
 * fileName       : CampaignSubOptionsRepository
 * author         : joy58
 * date           : 2024-12-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-03        joy58       최초 생성
 */
@Repository
interface CampaignSubOptionsRepository: JpaRepository<CampaignSubOptions, Long> {
}
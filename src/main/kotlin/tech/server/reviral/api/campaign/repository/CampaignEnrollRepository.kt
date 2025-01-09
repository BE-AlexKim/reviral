package tech.server.reviral.api.campaign.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.campaign.model.dto.CampaignDetailResponseDTO
import tech.server.reviral.api.campaign.model.entity.Campaign
import tech.server.reviral.api.campaign.model.entity.CampaignEnroll
import tech.server.reviral.api.campaign.model.entity.CampaignOptions
import tech.server.reviral.api.campaign.model.entity.CampaignSubOptions

/**
 *packageName    : tech.server.reviral.api.campaign.repository
 * fileName       : CampaignEnrollRepository
 * author         : joy58
 * date           : 2024-12-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-11        joy58       최초 생성
 */
@Repository
interface CampaignEnrollRepository: JpaRepository<CampaignEnroll, Long> {

    fun existsByCampaign(campaign: Campaign): Boolean

    fun findByCampaignAndUser(campaign: Campaign, user: User): List<CampaignEnroll?>

    fun countByUserAndCampaign(user: User, campaign: Campaign): Int

    fun findByUser(user: User): List<CampaignEnroll>?

}
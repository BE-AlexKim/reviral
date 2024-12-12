package tech.server.reviral.api.campaign.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.server.reviral.api.campaign.model.dto.CampaignDetailResponseDTO
import tech.server.reviral.api.campaign.model.entity.CampaignEnroll
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

    fun existsBySubOptions(subOption: CampaignSubOptions): Boolean

}
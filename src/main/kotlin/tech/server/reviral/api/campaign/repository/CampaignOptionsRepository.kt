package tech.server.reviral.api.campaign.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.campaign.model.entity.CampaignOptions

/**
 *packageName    : tech.server.reviral.api.campaign.repository
 * fileName       : CampaignOptionsRepository
 * author         : joy58
 * date           : 2024-12-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-03        joy58       최초 생성
 */
@Repository
interface CampaignOptionsRepository: JpaRepository<CampaignOptions, Long> {
    @Transactional
    override fun deleteById(id: Long)
}
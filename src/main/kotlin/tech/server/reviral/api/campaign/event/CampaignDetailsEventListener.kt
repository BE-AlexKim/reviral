package tech.server.reviral.api.campaign.event

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.campaign.model.entity.CampaignDetails
import tech.server.reviral.api.campaign.model.enums.SellerStatus
import tech.server.reviral.api.campaign.repository.CampaignDetailsRepository
import tech.server.reviral.common.config.response.exception.CampaignException
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.campaign.event
 * fileName       : CampaignDetailsEventListener
 * author         : joy58
 * date           : 2025-01-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-14        joy58       최초 생성
 */
@Component
class CampaignDetailsEventListener(
    private val campaignDetailsRepository: CampaignDetailsRepository
) {

    @Async
    @EventListener
    @Transactional(noRollbackFor = [CampaignException::class])
    @Throws(Exception::class)
    fun handleCampaignDetailsUpdate(event: CampaignDetailsUpdateEvent) {
        val campaignDetails = event.campaignDetails

        campaignDetails.sellerStatus = SellerStatus.COMPLETE
        campaignDetails.updateAt = LocalDateTime.now()
        this.campaignDetailsRepository.save(campaignDetails)

        val newCampaignDetails = campaignDetailsRepository.findByCampaignAndSortNo(campaignDetails.campaign!!, campaignDetails.sortNo + 1)

        if (newCampaignDetails != null) {
            newCampaignDetails.sellerStatus = SellerStatus.ACTIVE
            newCampaignDetails.updateAt = LocalDateTime.now()
            this.campaignDetailsRepository.save(newCampaignDetails)
        }
    }

    data class CampaignDetailsUpdateEvent(
        val campaignDetails: CampaignDetails
    )
}
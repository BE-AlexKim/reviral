package tech.server.reviral.api.campaign.repository.specification

import org.springframework.boot.autoconfigure.rsocket.RSocketProperties.Server.Spec
import org.springframework.data.jpa.domain.Specification
import tech.server.reviral.api.campaign.model.entity.Campaign
import tech.server.reviral.api.campaign.model.entity.CampaignDetails
import tech.server.reviral.api.campaign.model.enums.CampaignCategory
import tech.server.reviral.api.campaign.model.enums.CampaignPlatform
import tech.server.reviral.api.campaign.model.enums.CampaignStatus
import java.time.LocalDate

/**
 *packageName    : tech.server.reviral.api.campaign.repository.specification
 * fileName       : CampaignSpecification
 * author         : joy58
 * date           : 2024-12-05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-05        joy58       최초 생성
 */
object CampaignSpecification {

    fun equalCampaignId(campaignId: Long?): Specification<Campaign> = campaignId.let {
        Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<Long>("id"),it)
        }
    }

    fun platformContains(platform: String): Specification<Campaign> = platform.let {
        Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<CampaignPlatform>("campaignPlatform"),it.uppercase())
        }
    }

    fun equalCampaignStatus(status: String): Specification<Campaign> = status.let {
        Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<CampaignStatus>("campaignStatus"),it.uppercase())
        }
    }

    fun equalCampaignCategory(category: String): Specification<Campaign> = category.let {
        Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<CampaignCategory>("campaignCategory"),it.uppercase())
        }
    }

    fun betweenActiveDate(now: LocalDate = LocalDate.now() ): Specification<Campaign> = now.let {
        Specification { root, _, criteriaBuilder ->
            val join = root.join<Campaign, CampaignDetails>("details")
            criteriaBuilder.between(join.get("activeDate"), it,it.plusDays(1))
        }
    }

    fun betweenFinishDate(now: LocalDate = LocalDate.now()): Specification<Campaign> = now.let {
        Specification { root, _, criteriaBuilder ->
            val join = root.join<Campaign, CampaignDetails>("details")
            criteriaBuilder.between(join.get("finishDate"),it.minusDays(1), it)
        }
    }

}
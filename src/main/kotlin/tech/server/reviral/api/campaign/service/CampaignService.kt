package tech.server.reviral.api.campaign.service

import org.springframework.stereotype.Service
import tech.server.reviral.api.campaign.repository.CampaignRepository

/**
 *packageName    : tech.server.reviral.api.campaign.service
 * fileName       : CampaignService
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@Service
class CampaignService constructor(
    private val campaignRepository: CampaignRepository
){



}
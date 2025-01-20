package tech.server.reviral.api.campaign.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.server.reviral.api.account.model.dto.SignInRequestDTO
import tech.server.reviral.api.campaign.model.dto.AdminCampaignDetailsResponseDTO
import tech.server.reviral.api.campaign.model.dto.AdminCampaignsResponseDTO
import tech.server.reviral.api.campaign.service.CampaignAdminService
import tech.server.reviral.common.config.response.success.WrapResponseEntity
import tech.server.reviral.common.config.security.JwtToken

/**
 *packageName    : tech.server.reviral.api.campaign.controller
 * fileName       : CampaignAdminController
 * author         : joy58
 * date           : 2025-01-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-20        joy58       최초 생성
 */
@Tag(name = "캠페인 어드민", description = "어드민 캠페인 API")
@RestController
@RequestMapping("/api/v1/campaign/admin")
class CampaignAdminController constructor(
    private val campaignAdminService: CampaignAdminService
){

    @GetMapping("/list")
    fun getCampaigns(
        @RequestParam status: String?,
        @RequestParam platform: String?,
        @RequestParam campaignTitle: String?,
        pageable: Pageable
    ): ResponseEntity<WrapResponseEntity<MutableList<AdminCampaignsResponseDTO>>> {
        val campaigns = campaignAdminService.getCampaigns(status, platform, campaignTitle, pageable)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "campaigns", campaigns)
    }

    @GetMapping("/list/{campaignId}")
    fun getCampaignDetails(
        @PathVariable("campaignId") campaignId: Long
    ):ResponseEntity<WrapResponseEntity<MutableList<AdminCampaignDetailsResponseDTO>>> {
        val campaignDetails = campaignAdminService.getCampaignDetails(campaignId)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "campaignDetails", campaignDetails)
    }


}
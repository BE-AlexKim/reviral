package tech.server.reviral.api.campaign.controller

import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.server.reviral.api.campaign.model.dto.SaveCampaignRequestDTO
import tech.server.reviral.api.campaign.model.entity.Campaign
import tech.server.reviral.api.campaign.service.CampaignService
import tech.server.reviral.common.config.docs.campaign.SaveCampaignExplain
import tech.server.reviral.common.config.response.success.WrapResponseEntity

/**
 *packageName    : tech.server.reviral.api.campaign.controller
 * fileName       : CampaignController
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@RestController
@RequestMapping("/api/v1/campaign")
@Tag(name = "캠페인", description = "캠페인 정보 API")
class CampaignController constructor(
    private val campaignService: CampaignService
){

    /**
     * 캠페인 정보 다건 조회
     */
    @GetMapping
    fun getCampaigns(
        @RequestParam category: String?,
        @RequestParam platform: String?,
        @RequestParam campaignId: Long?,
        @RequestParam status: String?,
        pageable: Pageable
    ):ResponseEntity<WrapResponseEntity<Page<Campaign>>> {
        val search = campaignService.searchCampaigns(category, platform, status, campaignId, pageable)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "data", search)
    }

    /**
     * 캠페인 정보 단건 조회
     */
    @GetMapping("/{campaignId}")
    fun getCampaign(@PathVariable campaignId: Long): ResponseEntity<WrapResponseEntity<Boolean>> {
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "hello", true)
    }

    /**
     * 캠페인 정보 저장
     */
    @PostMapping("/save")
    @SaveCampaignExplain
    fun save(@RequestBody request: SaveCampaignRequestDTO ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val save = campaignService.setCampaign(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isSave", save)
    }

    /**
     * 캠페인 정보 단건 수정
     */
    @PutMapping("/{campaignId}")
    fun updateCampaign(@PathVariable campaignId: Long) {

    }

}
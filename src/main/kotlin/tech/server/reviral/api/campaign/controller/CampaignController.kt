package tech.server.reviral.api.campaign.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.server.reviral.api.campaign.model.dto.*
import tech.server.reviral.api.campaign.service.CampaignService
import tech.server.reviral.common.config.docs.campaign.SaveCampaignExplain
import tech.server.reviral.common.config.docs.campaign.SearchCampaignDetailsExplain
import tech.server.reviral.common.config.docs.campaign.SearchCampaignExplain
import tech.server.reviral.common.config.docs.campaign.UpdateCampaignExplain
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
     * @param category: String?
     * @param pageable: String?
     * @param status: String?
     * @param pageable: Pageable
     * @return CampaignCardResponseDTO
     */
    @GetMapping
    @SearchCampaignExplain
    fun getCampaigns(
        @RequestParam category: String?,
        @RequestParam platform: String?,
        @RequestParam status: String?,
        pageable: Pageable,
    ):ResponseEntity<WrapResponseEntity<List<CampaignCardResponseDTO>>> {
        val search = campaignService.searchCampaigns(category, platform, status,pageable)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "campaigns", search)
    }

    /**
     * 캠페인 정보 단건 조회
     * @param campaignId: Long
     * @return CampaignDetailResponseDTO
     */
    @GetMapping("/{campaignId}")
    @SearchCampaignDetailsExplain
    fun getCampaign(@PathVariable campaignId: Long): ResponseEntity<WrapResponseEntity<CampaignDetailResponseDTO>> {
        val campaign = campaignService.getCampaign(campaignId)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "campaign", campaign)
    }

    /**
     * 캠페인 정보 저장
     * @param request: SaveCampaignRequestDTO
     * @return Boolean
     */
    @PostMapping("/save")
    @SaveCampaignExplain
    fun save(@RequestBody request: SaveCampaignRequestDTO ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val save = campaignService.setCampaign(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isSave", save)
    }

    /**
     * 캠페인 참여 정보 등록
     * @param request: EnrollCampaignRequestDTO
     * @return Boolean
     */
    @PostMapping("/enroll")
    fun enroll(@RequestBody request: EnrollCampaignRequestDTO): ResponseEntity<WrapResponseEntity<Boolean>> {
        val enroll = campaignService.enrollCampaign(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isSave", enroll)
    }

    /**
     * 캠페인 정보 단건 수정
     * @param campaignId: Long
     * @param request: UpdateCampaignRequestDTO
     * @return Boolean
     */
    @PutMapping("/{campaignId}")
    @UpdateCampaignExplain
    fun updateCampaign(
        @PathVariable campaignId: Long,
        @RequestBody request: UpdateCampaignRequestDTO
    ):ResponseEntity<WrapResponseEntity<Boolean>> {
        val update = campaignService.updateCampaign(campaignId, request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isUpdated", update)
    }

}
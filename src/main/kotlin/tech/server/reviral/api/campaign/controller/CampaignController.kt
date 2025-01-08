package tech.server.reviral.api.campaign.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tech.server.reviral.api.campaign.model.dto.*
import tech.server.reviral.api.campaign.service.CampaignService
import tech.server.reviral.common.config.docs.campaign.*
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
    @EnrollCampaignExplain
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

    /**
     * 사용자 마이캠페인 정보 조회
     * @param loginId: String
     * @return ResponseEntity<WrapResponseEntity<Boolean>>
     */
    @GetMapping("/users/{userId}")
    @SearchMyCampaignExplain
    fun getMyCampaigns(@PathVariable userId: Long): ResponseEntity<WrapResponseEntity<MyCampaignResponseDTO>> {
        val myCampaigns = campaignService.getMyCampaigns(userId)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "myCampaigns", myCampaigns)
    }

    /**
     * 사용자 후기 작성 서비스
     * @param image: MultipartFile
     * @param request: EnrollReviewRequestDTO
     * @return Boolean: S3저장 성공 여부
     */
    @PostMapping(value = ["/review"], consumes = ["multipart/form-data"] )
    @UploadReviewExplain
    fun setReview(
        @RequestPart("image") image: MultipartFile,
        @RequestPart("request") request: EnrollReviewRequestDTO
    ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val review = campaignService.setReviewImage(image, request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isSave", review)
    }

    @PostMapping("/order")
    @UploadProductOrderNoExplain
    fun setOrderNo(@RequestBody request: EnrollProductOrderNoRequestDTO ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val isSave = campaignService.setProductOrderNo(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isSave", isSave)
    }
}
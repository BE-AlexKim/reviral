package tech.server.reviral.api.campaign.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tech.server.reviral.api.campaign.model.dto.*
import tech.server.reviral.api.campaign.service.CampaignService
import tech.server.reviral.config.docs.campaign.*
import tech.server.reviral.config.response.success.WrapResponseEntity

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
        @RequestParam status: String?,
        pageable: Pageable,
    ):ResponseEntity<WrapResponseEntity<List<CampaignCardResponseDTO>>> {
        val search = campaignService.searchCampaigns(status,pageable)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "campaigns", search)
    }

    /**
     * 캠페인 정보 단건 조회
     * @param campaignDetailsId: Long
     * @return CampaignDetailResponseDTO
     */
    @GetMapping("/{campaignDetailsId}")
    @SearchCampaignDetailsExplain
    fun getCampaign(@PathVariable campaignDetailsId: Long): ResponseEntity<WrapResponseEntity<CampaignDetailResponseDTO>> {
        val campaign = campaignService.getCampaignDetail(campaignDetailsId)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "campaign", campaign)
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
     * 사용자 마이캠페인 정보 조회
     * @param userId: Long
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
    @PostMapping(value = ["/review"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @UploadReviewExplain
    fun setReview(
        @RequestPart("image") image: MultipartFile,
        @ModelAttribute request: EnrollReviewRequestDTO
    ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val review = campaignService.setReviewImage(request, image)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isSave", review)
    }

    /**
     * 사용자 주문번호 등록 서비스
     * @param request: EnrollProductOrderNoRequestDTO
     * @return Boolean: 후기 작성 등록
     */
    @PostMapping("/order", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @UploadProductOrderNoExplain
    fun setProductOrderImageUrl(
        @RequestPart("image") image: MultipartFile,
        @ModelAttribute request: EnrollReviewRequestDTO
    ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val isSave = campaignService.setOrderImage(request, image)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isSave", isSave)
    }

    /**
     * 캠페인 등록 취소
     * @param request: DeleteCampaignEnrollRequestDTO
     * @return Boolean: 삭제여부
     */
    @DeleteMapping("/enroll")
    @DeleteEnrollCampaignExplain
    fun deleteCampaignEnroll(@RequestBody request: DeleteCampaignEnrollRequestDTO): ResponseEntity<WrapResponseEntity<Boolean>> {
        val delete = campaignService.deleteCampaignEnroll(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isDeleted", delete)
    }
}
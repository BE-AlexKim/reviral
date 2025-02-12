package tech.server.reviral.api.bussiness.controller

import io.jsonwebtoken.Jwt
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import tech.server.reviral.api.account.model.dto.SignInRequestDTO
import tech.server.reviral.api.bussiness.model.dto.AccountResultDTO
import tech.server.reviral.api.bussiness.model.dto.AdminSignupRequestDTO
import tech.server.reviral.api.bussiness.model.dto.PointExchangeIdsRequestDTO
import tech.server.reviral.api.bussiness.model.enums.ImageCheck
import tech.server.reviral.api.bussiness.service.BusinessService
import tech.server.reviral.api.campaign.model.dto.*
import tech.server.reviral.api.point.model.dto.PointExchangeResponseDTO
import tech.server.reviral.common.config.docs.bussiness.TransferCompleteExplain
import tech.server.reviral.common.config.docs.bussiness.TransferExplain
import tech.server.reviral.common.config.docs.bussiness.TransferResultExplain
import tech.server.reviral.common.config.docs.campaign.*
import tech.server.reviral.common.config.response.success.WrapResponseEntity
import tech.server.reviral.common.config.security.JwtToken
import kotlin.math.sign

/**
 *packageName    : tech.server.reviral.api.bussiness.controller
 * fileName       : CampaignController
 * author         : joy58
 * date           : 2025-01-31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-31        joy58       최초 생성
 */
@RestController
@RequestMapping("/api/v1/business")
class BusinessController constructor(
    private val businessService: BusinessService
) {

    /**
     * 어드민 캠페인 목록 조회
     * @param status: 상태값
     * @param platform: 플랫폼
     * @param campaignTitle: 캠페인 제목
     * @param size: 조회건 수
     * @param page: 조회 페이지 수
     */
    @Tag(name = "어드민 캠페인 API")
    @GetCampaignsExplain
    @GetMapping("/campaigns")
    fun getCampaigns(
        @RequestParam status: String?,
        @RequestParam platform: String?,
        @RequestParam campaignTitle: String?,
        @RequestParam size: Long,
        @RequestParam page: Long
    ): ResponseEntity<WrapResponseEntity<AdminCampaignsResponseDTO>> {
        val campaigns = businessService.getCampaigns(status, platform, campaignTitle, size, page)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "campaign", campaigns)
    }

    /**
     * 캠페인 일자별 상세 정보 조회
     * @param campaignId: 캠페인 일련번호
     * @return 캠페인 상세 정보 응답 모델
     */
    @Tag(name = "어드민 캠페인 API")
    @GetMapping("/campaigns/{campaignId}")
    @GetCampaignDetailsExplain
    fun getCampaignDetails(
        @PathVariable("campaignId") campaignId: Long,
    ):ResponseEntity<WrapResponseEntity<MutableList<AdminCampaignDetailsResponseDTO>>> {
        val campaignDetails = businessService.getCampaignDetails(campaignId)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "campaignDetails", campaignDetails)
    }

    /**
     * 캠페인 정보 저장
     * @param request: 캠페인 정보 등록 모델
     * @return Boolean
     */
    @Tag(name = "어드민 캠페인 API")
    @PostMapping("/campaign", consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE])
    @SaveCampaignExplain
    fun setCampaign(
        @RequestPart("images") images: List<MultipartFile> = emptyList(),
        @RequestBody request: SaveCampaignRequestDTO
    ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val save = businessService.setCampaign(images, request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isSave", save)
    }

    /**
     * 캠페인 정보 단건 수정
     * @param request: UpdateCampaignRequestDTO
     * @return Boolean
     */
    @Tag(name = "어드민 캠페인 API")
    @PutMapping("/campaign/edit")
    @UpdateCampaignExplain
    fun updateCampaign(
        @RequestBody request: UpdateCampaignRequestDTO
    ):ResponseEntity<WrapResponseEntity<Boolean>> {
        val update = businessService.updateCampaign(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isUpdated", update)
    }

    /**
     * 캠페인 참여자 상세 정보 단건 조회
     * @param request: 캠페인 상세정보 요청 모델
     * @return 캠페인 상세정보 응답 모델
     */
    @Tag(name = "어드민 캠페인 API")
    @GetCampaignDetailExplain
    @PostMapping("/campaign/detail")
    fun getCampaignDetail(@RequestBody request: CampaignDetailRequestDTO): ResponseEntity<WrapResponseEntity<AdminCampaignDetailResponseDTO?>> {
        val campaignDetail = businessService.getCampaignDetail(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK,"campaignDetail", campaignDetail)
    }

    /**
     * 모집 마감 처리 및 사용자 구매 진행 처리
     * @param request: 캠페인 시작 모델
     * @return Boolean: 성공 실패 유무
     */
    @Tag(name = "어드민 캠페인 API")
    @PostMapping("/campaign/begin")
    @StartCampaignExplain
    fun startCampaign( @RequestBody request: CampaignStartRequestDTO ):ResponseEntity<WrapResponseEntity<Boolean>> {
        val start = businessService.setCampaignDetails(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isStart", start)
    }

    /**
     * 캠페인 개별 시작 처리
     * @param request: 캠페인 개별 시작 요청 모델
     * @return Boolean: 성공 실패 유무
     */
    @Tag(name = "어드민 캠페인 API")
    @PostMapping("/campaign/begin/separate")
    fun startCampaignUsers(
        @RequestBody request: StartCampaignUserRequestDTO
    ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val start = businessService.setCampaignStart(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isStart", start)
    }

    @Tag(name = "어드민 캠페인 API")
    @CheckCampaignExplain
    @PostMapping("/campaign/enroll/check/{imageCheckStatus}")
    fun getInspect(
        @PathVariable imageCheckStatus: ImageCheck,
        @RequestBody request: CampaignCheckRequestDTO
    ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val check = businessService.setCampaignImageStatus(imageCheckStatus, request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK,"isCheck", check)
    }

/**
     * 캠페인 시작하기
     * @param request: 등록한 캠페인 시작
     * @return 등록 성공유무
     */
    @Tag(name = "어드민 캠페인 API")
    @PostMapping("/campaign/enroll/start")
    fun play(@RequestBody request: CampaignEnrollPlayRequestDTO): ResponseEntity<WrapResponseEntity<Boolean>> {
        val play = businessService.startCampaign(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isPlay", play)
    }

    /**
     * 캠페인 수정 정보 조회
     * @param campaignId: 캠페인 일련번호
     * @return 캠페인 정보 조회
     */
    @Tag(name = "어드민 캠페인 API")
    @GetMapping("/campaign/edit")
    fun getCampaign(
        @RequestParam campaignId: Long
    ): ResponseEntity<WrapResponseEntity<GetCampaignResponseDTO?>> {
        val campaign = businessService.getCampaign(campaignId)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "campaign", campaign)
    }

    @Tag(name = "어드민 포인트 API")
    @GetMapping("/point/transfer")
    @TransferExplain
    fun getExchangeConvertExcel(): ResponseEntity<WrapResponseEntity<List<PointExchangeResponseDTO>>> {
        val exchanges = businessService.getExchangeConvertExcel()
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "list", exchanges)
    }

    @Tag(name = "어드민 포인트 API")
    @PostMapping("/point/transfer/complete")
    @TransferCompleteExplain
    fun setExchangeDownload(@RequestBody request: PointExchangeIdsRequestDTO): ResponseEntity<WrapResponseEntity<Boolean>> {
        val complete = businessService.setPointExchangeDownload(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK,"isComplete", complete)
    }

    @Tag(name = "어드민 포인트 API")
    @PostMapping("/point/transfer/result")
    @TransferResultExplain
    fun setPointExchange( @RequestBody request: List<AccountResultDTO> ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val isExchange = businessService.setPointExchange(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK,"isExchange", isExchange)
    }

    @Tag(name = "어드민 계정 API")
    @PostMapping("/signup")
    fun signup(@RequestBody request: AdminSignupRequestDTO): ResponseEntity<WrapResponseEntity<Boolean>> {
        val signup = businessService.signup(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "signup", signup)
    }

    @Tag(name = "어드민 계정 API")
    @PostMapping("/sign-in")
    fun login(@RequestBody request: SignInRequestDTO): ResponseEntity<WrapResponseEntity<JwtToken>> {
        val login = businessService.login(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "jwt", login)
    }

}
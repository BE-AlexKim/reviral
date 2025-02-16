package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.campaign.model.enums.CampaignCategory
import tech.server.reviral.api.campaign.model.enums.CampaignPlatform
import tech.server.reviral.api.campaign.model.enums.OptionType
import java.time.LocalDate

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : UpdateCamapginRequestDTO
 * author         : joy58
 * date           : 2024-12-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-12        joy58       최초 생성
 */
@Schema(name = "캠페인 수정 요청 모델")
data class UpdateCampaignRequestDTO(
    @Schema(description = "캠페인 일련번호", required = true)
    val campaignId: Long,

    @Schema(description = "업체명", example = "몽테르", required = true)
    val companyName: String? = null,

    @Schema(description = "캠페인 플랫폼", example = "NAVER, COUPANG, ETC", required = true)
    val platform: CampaignPlatform? = null,

    @Schema(description = "캠페인 카테고리", example = "AB(빈박스),PT(포토리뷰),EX(체험단)", required = true)
    val category: CampaignCategory? = null,

    @Schema(description = "상품명", example = "칠초 무릎담요 극세사 캠핑 비행기 블랭킷", required = true)
    val productTitle: String? = null,

    @Schema(description = "상품 이미지 링크", example = "https://naver.com", required = true)
    val campaignImgUrl: String? = null,

    @Schema(description = "옵션 타입", example = "SINGLE, MULTI", required = true)
    val optionType: OptionType? = null,

    @Schema(description = "상품 옵션", required = false)
    val options: MutableList<Options>? = null,

    @Schema(description = "셀러 모집글", required = true)
    val sellerRequest: String? = null,

) {
    @Schema(name = "상품 옵션")
    data class Options(
        @Schema(description = "옵션명", example = "털모자")
        val optionTitle: String? = null,
        @Schema(description = "총 모집 인원", example = "100")
        val recruitPeople: Int? = null,
        @Schema(description = "하위 옵션")
        val subOption: MutableList<SubOptions>? = null
    )

    @Schema(name = "하위 옵션")
    data class SubOptions(
        @Schema(description = "하위 옵션명", example = "L 사이즈")
        val subOptionTitle: String? = null,
        @Schema(description = "추가 금액", example = "5000")
        val addPrice: Int? = null,
        @Schema(description = "옵션 모집 인원", example = "100")
        val recruitPeople: Int? = null
    )
}


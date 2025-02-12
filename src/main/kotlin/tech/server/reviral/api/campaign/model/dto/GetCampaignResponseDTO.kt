package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.campaign.model.enums.CampaignCategory
import tech.server.reviral.api.campaign.model.enums.CampaignPlatform
import tech.server.reviral.api.campaign.model.enums.OptionType
import java.time.LocalDate

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : GetCampaignResponseDTO
 * author         : joy58
 * date           : 2025-01-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-23        joy58       최초 생성
 */
data class GetCampaignResponseDTO(
    @Schema(description = "업체명", example = "몽테르", required = true)
    val companyName: String? = null,

    @Schema(description = "캠페인 플랫폼", example = "NAVER, COUPANG, ETC", required = true)
    val platform: CampaignPlatform? = null,

    @Schema(description = "캠페인 카테고리", example = "TIME, DAILY", required = true)
    val category: CampaignCategory? = null,

    @Schema(description = "시간 구매 상품 시작 시간 값", example = "12:00", required = false)
    val startTime: String? = null,

    @Schema(description = "시간 구매 상품 종료 시간 값", example = "14:00", required = false)
    val endTime: String? = null,

    @Schema(description = "상품명", example = "칠초 무릎담요 극세사 캠핑 비행기 블랭킷", required = true)
    val productTitle: String? = null,

    @Schema(description = "상품 이미지 링크", example = "https://naver.com", required = true)
    val campaignImgUrl: String? = null,

    @Schema(description = "상품 링크", example = "https://naver.com", required = true)
    val campaignLink: String? = null,

    @Schema(description = "상품 가격", example = "8900", required = true)
    val campaignPrice: Int? = null,

    @Schema(description = "리뷰어 지급 포인트", example = "500", required = true)
    val reviewPoint: Int? = null,

    @Schema(description = "캠페인 진행비용", example = "3000", required = true)
    val progressPrice: Int? = null,

    @Schema(description = "판매 시작 날짜", example = "2024.12.31", required = true)
    val startSaleDateTime: LocalDate? = null,

    @Schema(description = "판매 종료 날짜", example = "2025.01.31", required = true)
    val endSaleDateTime: LocalDate? = null,

    @Schema(description = "상품 옵션", required = false)
    var options: List<Options> = emptyList(),

    @Schema(description = "셀러 모집글", required = true)
    val sellerRequest: String? = null,

    @Schema(description = "셀러 구매가이드", required = true)
    val sellerGuide: String? = null,

    @Schema(description = "옵션 타입", example = "SINGLE, MULTI", required = true)
    val optionType: OptionType? = null,

) {
    @Schema(name = "상품 옵션")
    data class Options(
        @Schema(description = "옵션명", example = "털모자")
        val optionTitle: String? = null,
        @Schema(description = "총 모집 인원", example = "100")
        val recruitPeople: Int? = null,
        @Schema(description = "하위 옵션")
        val subOptions: List<SubOptions>? = null
    )

    @Schema(name = "하위 옵션")
    data class SubOptions(
        @Schema(description = "하위 옵션명", example = "L 사이즈")
        val subOptionTitle: String? = null,
        @Schema(description = "추가 금액", example = "5000")
        val addPrice: Int? = null,
        @Schema(description = "옵션 모집 인원", example = "100")
        val subRecruitPeople: Int? = null
    )
}




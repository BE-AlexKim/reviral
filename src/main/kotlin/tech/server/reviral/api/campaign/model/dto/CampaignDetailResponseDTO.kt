package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.campaign.model.enums.CampaignCategory

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : CampaignDetailResponseDTO
 * author         : joy58
 * date           : 2024-12-10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-10        joy58       최초 생성
 */
@Schema(name = "캠페인 상세정보 응답 모델")
data class CampaignDetailResponseDTO(
    @Schema(description = "캠페인 상세 일련번호")
    val campaignDetailsId: Long? = null,
    @Schema(description = "캠페인 제목")
    val campaignTitle: String = "",
    @Schema(description = "캠페인 카테고리(당일구매,시간구매)")
    val campaignCategory: CampaignCategory = CampaignCategory.DAILY,
    @Schema(description = "캠페인 링크")
    val campaignUrl: String = "",
    @Schema(description = "캠페인 이미지 링크")
    val campaignImgUrl: String = "",
    @Schema(description = "캠페인 가격")
    val campaignPrice: Int = 0,
    @Schema(description = "캠페인 진행포인트")
    val campaignPoint: Int = 0,
    @Schema(description = "셀러 요청사항")
    val sellerRequest: String = "",
    @Schema(description = "캠페인 모집인원")
    val totalCount: Int = 0,
    @Schema(description = "캠페인 참여인원")
    val joinCount: Int = 0,
    @Schema(description = "옵션")
    var options: List<Options?> = listOf()
) {
    data class Options(
        val campaignOptionsId: Long = 0,
        val optionTitle: String? = "",
        var subOptions: List<SubOptions?> = emptyList()
    )

    data class SubOptions (
        val campaignSubOptionsId: Long? = null,
        val campaignAddPrice: Int? = null,
        val campaignSubOptionTitle: String? = null
    )

}
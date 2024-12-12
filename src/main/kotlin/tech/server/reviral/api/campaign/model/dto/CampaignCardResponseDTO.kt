package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.campaign.model.enums.CampaignPlatform
import tech.server.reviral.api.campaign.model.enums.CampaignStatus

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : CampaignCardResponsDTO
 * author         : joy58
 * date           : 2024-12-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-09        joy58       최초 생성
 */
@Schema(name = "캠페인 카드 응답 모델")
data class CampaignCardResponseDTO(
    @Schema(description = "캠페인 일련번호")
    val campaignId: Long? = null,
    @Schema(description = "캠페인 타이틀")
    val campaignTitle: String? = null,
    @Schema(description = "캠페인 상태값")
    val campaignStatus: CampaignStatus? = null,
    @Schema(description = "캠페인 플랫폼")
    val campaignPlatform: CampaignPlatform? = null,
    @Schema(description = "캠페인 이미지 링크")
    val campaignImgUrl: String? = null,
    @Schema(description = "캠페인 마감기간")
    val period: Int = 0,
    @Schema(description = "캠페인 상품가격")
    val campaignPrice: Int = 0,
    @Schema(description = "캠페인 포인트")
    val campaignPoint: Int = 0 ,
    @Schema(description = "캠페인 모집인원")
    val totalCount: Int = 0 ,
    @Schema(description = "캠페인 참여인원")
    val joinCount: Int = 0,
)

package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.campaign.model.enums.CampaignPlatform
import tech.server.reviral.api.campaign.model.enums.CampaignStatus
import tech.server.reviral.api.campaign.model.enums.SellerStatus
import java.time.LocalDate

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
    val campaignDetailsId: Long? = null,
    @Schema(description = "캠페인 타이틀")
    val campaignTitle: String? = null,
    @Schema(description = "캠페인 상태값")
    val campaignStatus: CampaignStatus? = null,
    @Schema(description = "캠페인 플랫폼")
    val campaignPlatform: CampaignPlatform? = null,
    @Schema(description = "캠페인 이미지 링크")
    val campaignImgUrl: String? = null,
    @Schema(description = "캠페인 마감기간")
    val period: Long = 0,
    @Schema(description = "캠페인 상품가격")
    val campaignPrice: Int = 0,
    @Schema(description = "캠페인 포인트")
    val campaignPoint: Int = 0,
    @Schema(description = "캠페인 모집인원")
    val totalCount: Long = 0,
    @Schema(description = "캠페인 참여인원")
    val joinCount: Long = 0,
    @Schema(description = "오픈 날짜")
    val applyDate: LocalDate? = null,
    @Schema(description = "캠페인 상세 상태")
    val sellerStatus: SellerStatus? = null,
    @Schema(description = "와우 회원전용")
    val cpType: Boolean? = false,
)

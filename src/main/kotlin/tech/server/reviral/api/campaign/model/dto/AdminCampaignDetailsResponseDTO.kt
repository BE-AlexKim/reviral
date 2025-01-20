package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.campaign.model.enums.SellerStatus
import java.time.LocalDate

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : AdminCampaignDetailsResponseDTO
 * author         : joy58
 * date           : 2025-01-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-20        joy58       최초 생성
 */
@Schema(name = "캠페인 상세정보 응답 모델")
data class AdminCampaignDetailsResponseDTO(
    @Schema(description = "캠페인 상세 일련번호")
    val campaignDetailsId: Long? = null,
    @Schema(description = "셀러 상태값")
    val sellerStatus: SellerStatus = SellerStatus.WAIT,
    @Schema(description = "캠페인 날짜")
    val applyDate: LocalDate = LocalDate.now(),
    @Schema(description = "캠페인 제목")
    val campaignTitle: String = "",
    @Schema(description = "캠페인 가격")
    val campaignPrice: Long = 0
)

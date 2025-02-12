package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : CampaignStartRequestDTO
 * author         : joy58
 * date           : 2025-01-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-21        joy58       최초 생성
 */
@Schema(name = "캠페인 시작요청 모델")
data class CampaignStartRequestDTO(
    @Schema(description = "캠페인 상세 일련번호")
    val campaignDetailsId: Long
)

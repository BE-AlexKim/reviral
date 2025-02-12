package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : CampaignEnrollPlayRequestDTO
 * author         : joy58
 * date           : 2025-01-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-22        joy58       최초 생성
 */
@Schema(name = "캠페인 시작 요청 모델")
data class CampaignEnrollPlayRequestDTO(
    @Schema(description = "캠페인 상세 일련번호")
    val campaignDetailsId: Long
)

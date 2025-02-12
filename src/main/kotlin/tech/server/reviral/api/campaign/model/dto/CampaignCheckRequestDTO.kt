package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : CampaignCheckRequestDTO
 * author         : joy58
 * date           : 2025-01-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-22        joy58       최초 생성
 */
@Schema(name = "캠페인 검수 결과 요청 모델")
data class CampaignCheckRequestDTO(
    @Schema(description = "사용자 일련번호")
    val userId: Long,
    @Schema(description = "등록 일련번호")
    val campaignEnrollId: Long
)

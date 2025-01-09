package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : DeleteCampaignEnrollRequestDTO
 * author         : joy58
 * date           : 2025-01-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-09        joy58       최초 생성
 */
@Schema(name = "캠페인 취소 요청 모델")
data class DeleteCampaignEnrollRequestDTO(
    @Schema(description = "사용자 일련번호")
    val userId: Long,
    @Schema(description = "사용자 캠페인 등록 일련번호")
    val campaignEnrollId: Long,
    @Schema(description = "강제 변경 유무")
    val isForcedRevision: Boolean = false
)

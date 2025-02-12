package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : StartCampaignUserRequestDTO
 * author         : joy58
 * date           : 2025-01-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-23        joy58       최초 생성
 */
@Schema(name = "캠페인 시작요청 모델")
data class StartCampaignUserRequestDTO(
    @Schema(description = "등록 일련번호 리스트")
    val enrollIds: List<Long>
)

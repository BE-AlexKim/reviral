package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : EnrollCampaignResponseDTO
 * author         : joy58
 * date           : 2024-12-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-11        joy58       최초 생성
 */
@Schema(name = "캠페인 참여 등록 요청 모델")
data class EnrollCampaignRequestDTO(
    @Schema(description = "사용자 일련번호")
    val userId: Long,
    @Schema(description = "캠페인 일련번호")
    val campaignId: Long,
    @Schema(description = "캠페인 캠페인 옵션 일련번호")
    val campaignOptionId: Long,
    @Schema(description = "캠페인 캠페인 서브옵션 이련번호")
    val campaignSubOptionId: Long? = null,
)

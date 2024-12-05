package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : CampaignResponseDTO
 * author         : joy58
 * date           : 2024-12-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-03        joy58       최초 생성
 */
@Schema(name = "메인 캠페인 목록")
data class CampaignResponseDTO(
    val limitCampaign: MutableList<LimitCampaign>
) {
    data class LimitCampaign(
        val campaignId: Long,
        val campaignTitle: String,
        val reviewPoint: Int,
        val recruitPeople: Int,
        val totalPeople: Int,
        val remainDate: Int,
    )
}

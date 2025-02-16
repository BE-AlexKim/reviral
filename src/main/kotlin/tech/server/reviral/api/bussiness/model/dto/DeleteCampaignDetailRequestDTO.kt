package tech.server.reviral.api.bussiness.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.bussiness.model.dto
 * fileName       : DeleteCampaignDetailRequestDTO
 * author         : joy58
 * date           : 2025-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-13        joy58       최초 생성
 */
@Schema(name = "캠페인 날짜 삭제 요청 모델")
data class DeleteCampaignDetailRequestDTO(
    @Schema(description = "캠페인 상세정보 일련번호")
    val campaignDetailsId: Long
)

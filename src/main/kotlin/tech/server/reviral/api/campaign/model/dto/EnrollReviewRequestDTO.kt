package tech.server.reviral.api.campaign.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : EnrollReviewRequestDTO
 * author         : joy58
 * date           : 2025-01-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-07        joy58       최초 생성
 */
@Schema(name = "후기 작성 요청 모델")
data class EnrollReviewRequestDTO(
    @Schema(description = "사용자 일련번호")
    val userId: Long,
    @Schema(description = "캠페인 등록 일련번호")
    val campaignEnrollId: Long,
)

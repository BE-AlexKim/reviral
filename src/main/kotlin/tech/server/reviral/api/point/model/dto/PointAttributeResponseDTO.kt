package tech.server.reviral.api.point.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.point.model.dto
 * fileName       : PointAttributeResponseDTO
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
@Schema(name = "포인트 내역 정보 응답 모델")
data class PointAttributeResponseDTO(
    @Schema(description = "포인트 환전 내역 건수")
    val changeCount: Int = 0,
    @Schema(description = "포인트 전환 완료 건수")
    val completeCount: Int = 0,
    @Schema(description = "포인트 전환 완료 건수")
    val expectCount: Int = 0,
)

package tech.server.reviral.api.point.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.point.model.dto
 * fileName       : ExchangePointRequestDTO
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
@Schema(name = "포인트 전환 요청 모델")
data class ExchangePointRequestDTO(
    @Schema(description = "사용자 일련번호")
    val userId: Long = 0,
    @Schema(description = "포인트 전환액수")
    val exchangeValue: Int = 0,
)

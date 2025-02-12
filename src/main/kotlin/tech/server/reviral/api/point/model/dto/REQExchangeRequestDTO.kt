package tech.server.reviral.api.point.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.point.model.dto
 * fileName       : REQExchangeRequestDTO
 * author         : joy58
 * date           : 2025-01-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-24        joy58       최초 생성
 */
@Schema(name = "포인트 전환 요청 목록 조회 모델")
data class REQExchangeRequestDTO(
    val pointConvertId: Long
)

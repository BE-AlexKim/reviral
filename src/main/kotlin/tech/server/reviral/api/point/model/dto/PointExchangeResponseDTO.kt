package tech.server.reviral.api.point.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.account.model.enums.BankCode

/**
 *packageName    : tech.server.reviral.api.point.model.dto
 * fileName       : PointExchangeResponseDTO
 * author         : joy58
 * date           : 2025-01-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-24        joy58       최초 생성
 */
@Schema(name = "목록")
data class PointExchangeResponseDTO(
    @Schema(description = "포인트 환전 내역 일련번호")
    val pointExchangeId: Long?,
    @Schema(description = "포인트 환전 내역 일련번호")
    val bankCode: String?,
    @Schema(description = "계좌번호")
    val account: String?,
    @Schema(description = "이체금액")
    val pointValue: Int?,
    @Schema(description = "예금주명")
    val name: String?,
    @Schema(description = "입금메모")
    val depositMemo: String?,
    @Schema(description = "출금메모")
    val withdrawMemo: String?
)
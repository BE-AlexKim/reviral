package tech.server.reviral.api.bussiness.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.bussiness.model.dto
 * fileName       : AccountResultDTO
 * author         : joy58
 * date           : 2025-02-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-03        joy58       최초 생성
 */
@Schema(name = "다계좌 이체 송부 결과 내역 정보 요청 모델")
data class AccountResultDTO(
    @Schema(description = "은행명")
    val bankName: String,
    @Schema(description = "계좌번호")
    val bankCode: String,
    @Schema(description = "가격")
    val price: String,
    @Schema(description = "계좌 소유주 명")
    val name: String,
    @Schema(description = "이체 결과")
    val status: String,
    @Schema(description = "고유 일련번호")
    val uniqueKey: String,
)

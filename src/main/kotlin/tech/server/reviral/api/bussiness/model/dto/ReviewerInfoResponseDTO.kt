package tech.server.reviral.api.bussiness.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.bussiness.model.dto
 * fileName       : ReviewerInfoResponseDTO
 * author         : joy58
 * date           : 2025-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-13        joy58       최초 생성
 */
data class ReviewerInfoResponseDTO(
    @Schema(description = "사용자 일련번호")
    val userId: Long,
    @Schema(description = "사용자 이름")
    val name: String?,
    @Schema(description = "사용자 로그인 아이디")
    val loginId: String,
    @Schema(description = "휴대폰 번호")
    val phoneNumber: String?,
    @Schema(description = "네이버 아이디")
    val nvId: String?,
    @Schema(description = "쿠팡 아이디")
    val cpId: String?,
    @Schema(description = "주소")
    val address: String?,
    @Schema(description = "계좌은행")
    val bankCode: String?,
    @Schema(description = "계좌번호")
    val accountNumber: String?
)

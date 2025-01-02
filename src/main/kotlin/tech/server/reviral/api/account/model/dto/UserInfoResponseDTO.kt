package tech.server.reviral.api.account.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.account.model.dto
 * fileName       : UserInfoResponseDTO
 * author         : joy58
 * date           : 2025-01-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-02        joy58       최초 생성
 */
@Schema(name = "사용자 개인정보 응답 모델")
data class UserInfoResponseDTO(
    @Schema(description = "휴대폰 번호")
    val phoneNumber: String,
    @Schema(description = "주소")
    val address: String,
    @Schema(description = "계좌은행")
    val bankCode: String?,
    @Schema(description = "계좌번호")
    val accountNumber: String?
)

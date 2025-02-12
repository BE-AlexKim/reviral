package tech.server.reviral.api.account.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.account.model.dto
 * fileName       : UpdateUserInfoRequestDTO
 * author         : joy58
 * date           : 2025-01-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-09        joy58       최초 생성
 */
@Schema(name = "사용자 개인내역 업데이트 요청 모델")
data class UpdateUserInfoRequestDTO(
    @Schema(description = "휴대폰 번호", required = false)
    val phoneNumber: String? = null,
    @Schema(description = "네이버 아이디", required = false)
    val nvId: String? = null,
    @Schema(description = "쿠팡 아이디", required = false)
    val cpId: String? = null,
    @Schema(description = "주소", required = false)
    val address: String? = null,
    @Schema(description = "은행코드", required = false)
    val bankCode: String? = null,
    @Schema(description = "계좌번호", required = false)
    val accountNumber: String? = null,
    @Schema(description = "2차 비밀번호", required = false)
    val password: String? = null
)

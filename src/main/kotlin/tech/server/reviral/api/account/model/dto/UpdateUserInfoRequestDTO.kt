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
    val userId: Long,
    val phoneNumber: String? = null,
    val nvId: String? = null,
    val cpId: String? = null,
    val address: String? = null,
    val bankCode: String? = null,
    val accountNumber: String? = null,
    val loginPw: String? = null,
    val pointPw: String? = null,
    val code: String? = null
)

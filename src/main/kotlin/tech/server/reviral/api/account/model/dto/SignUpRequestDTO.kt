package tech.server.reviral.api.account.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.account.model.dto
 * fileName       : SignUpRequestDTO
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
@Schema(name = "사용자 회원가입 요청 객체")
data class SignUpRequestDTO(
    val loginId: String,
    val loginPw: String,
    val address: String,
    val userRole: String,
    val phoneNumber: String,
    val pointPw: String
)

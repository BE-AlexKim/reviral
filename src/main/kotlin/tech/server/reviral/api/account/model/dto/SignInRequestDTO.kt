package tech.server.reviral.api.account.model.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 *packageName    : tech.server.reviral.api.account.model.dto
 * fileName       : LoginRequestDTO
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@Schema(name = "로그인 인증객체")
data class SignInRequestDTO(
    @Schema(description = "로그인 아이디")
    val loginId: String,
    @Schema(description = "로그인 비밀번호")
    val password: String
)

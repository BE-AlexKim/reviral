package tech.server.reviral.api.bussiness.model.dto

/**
 *packageName    : tech.server.reviral.api.bussiness.model.dto
 * fileName       : AdminSignupRequestDTO
 * author         : joy58
 * date           : 2025-02-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-09        joy58       최초 생성
 */
data class AdminSignupRequestDTO(
    val loginId: String,
    val loginPw: String
)
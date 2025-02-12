package tech.server.reviral.api.account.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.account.model.enums.Gender

/**
 *packageName    : tech.server.reviral.api.account.model.dto
 * fileName       : UserInfoRequestDTO
 * author         : joy58
 * date           : 2025-02-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-08        joy58       최초 생성
 */
@Schema(name = "사용자 초기 정보 등록 모델")
data class BasicUserInfoRequestDTO(
    @Schema(description = "사용자 일련번호")
    val userId: Long,
    @Schema(description = "사용자 성함")
    val name: String,
    @Schema(description = "사용자 2차 비밀번호")
    val password: String,
    @Schema(description = "휴대폰번호(-없이)")
    val phoneNumber: String,
    @Schema(description = "gender", example = "MAN, WOMEN")
    val gender: Gender,
    @Schema(description = "이벤트 수신동의")
    val isEvent: Boolean,
)

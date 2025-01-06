package tech.server.reviral.api.point.model.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.point.model.enums.ExchangeStatus
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.point.model.dto
 * fileName       : PointAttributeResponseDTO
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
@Schema(name = "포인트 내역 정보 응답 모델")
data class PointAttributeResponseDTO(
   val user: UserInfo,
   val pointHistory: List<PointAttr>?
) {
    @Schema(name = "포인트 사용자 정보 모델")
    data class UserInfo(
        @Schema(description = "사용자 성명")
        val name: String? = null,
        @Schema(description = "사용자 로그인 아이디")
        val loginId: String? = null,
        @Schema(description = "적립 가능 포인트")
        val expectPoint: Int? = null,
        @Schema(description = "총 전환 포인트")
        val totalPoint: Int? = null,
        @Schema(description = "포인트 잔액")
        val remainPoint: Int? = null
    )

    @Schema(name = "포인트 사용자 목록 정보")
    data class PointAttr(
        @Schema(description = "포인트 상태")
        val pointStatus: ExchangeStatus,
        @Schema(description = "신청/전환 일자")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        val createAt: LocalDateTime,
        @Schema(description = "포인트 설명")
        val exchangeDesc: String,
    )
}

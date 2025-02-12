package tech.server.reviral.api.campaign.model.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.campaign.model.enums.EnrollStatus
import tech.server.reviral.api.campaign.model.enums.ImageStatus
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : MyCampaignResponseDTO
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
@Schema(name = "나의 캠페인 목록 정보 모델")
data class MyCampaignResponseDTO(
    @Schema(name = "마이 캠페인 사용자 정보 모델")
    val userInfo: MyCampaignUserInfo? = null,
    @Schema(name = "캠페인 목록")
    val myCampaigns: List<MyCampaigns?>? = emptyList()
) {
    @Schema(name = "마이 캠페인 사용자 정보 모델")
    data class MyCampaignUserInfo(
        @Schema(description = "사용자 성명")
        val name: String? = null,
        @Schema(description = "사용자 로그인 아이디")
        val loginId: String? = null,
        @Schema(description = "적립 가능 포인트")
        val expectPoint: Int? = null,
        @Schema(description = "전환 예정 포인트")
        val exchangePoint: Int? = 0,
        @Schema(description = "총 전환 포인트")
        val totalPoint: Int? = null,
        @Schema(description = "포인트 잔액")
        val remainPoint: Int? = null
    )

    @Schema(name = "캠페인 목록")
    data class MyCampaigns(
        @Schema(description = "캠페인 일련번호")
        val campaignEnrollId: Long?,
        @Schema(description = "캠페인 진행상태")
        val campaignStatus: EnrollStatus?,
        @Schema(description = "캠페인 신청 일자")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        val registerDate: LocalDateTime?,
        @Schema(description = "캠페인 이미지")
        val campaignImgUrl: String?,
        @Schema(description = "캠페인 링크")
        val campaignLink: String?,
        @Schema(description = "캠페인 제목")
        val campaignTitle: String?,
        @Schema(description = "캠페인 셀러 요청사항")
        val sellerGuide: String?,
        @Schema(description = "주문번호 이미지 검수상태")
        val orderStatus: ImageStatus? = null,
        @Schema(description = "후기 이미지 검수상태")
        val reviewStatus: ImageStatus? = null
    )
}

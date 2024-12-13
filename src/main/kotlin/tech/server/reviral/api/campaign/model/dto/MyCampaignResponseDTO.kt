package tech.server.reviral.api.campaign.model.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.campaign.model.enums.EnrollStatus
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
    @Schema(description = "참여 신청 개수")
    val joinCount: Int? = 0,

    @Schema(description = "캠페인 진행 개수")
    val progressCount: Int? = 0,

    @Schema(description = "후기 작성 개수")
    val reviewCount: Int? = 0,

    @Schema(description = "캠페인 검수 개수")
    val inspectCount: Int? = 0,
    @Schema(name = "마이 캠페인 사용자 정보 모델")
    val userInfo: MyCampaignUserInfo? = null,
    @Schema(name = "캠페인 목록")
    val myCampaigns: List<MyCampaigns?>? = emptyList()
) {
    @Schema(name = "마이 캠페인 사용자 정보 모델")
    data class MyCampaignUserInfo(
        @Schema(description = "사용자 성명")
        val username: String? = null,
        @Schema(description = "사용자 로그인 아이디")
        val loginId: String? = null,
        @Schema(description = "이번달 적립 포인트")
        val expectPoint: Int? = null,
        @Schema(description = "총 전환 포인트")
        val changeTotalPoint: Int? = null,
        @Schema(description = "포인트 잔액")
        val userPoint: Int? = null
    )

    @Schema(name = "캠페인 목록")
    data class MyCampaigns(
        @Schema(description = "캠페인 일련번호")
        val campaignId: Long?,
        @Schema(description = "캠페인 진행상태")
        val campaignStatus: EnrollStatus?,
        @Schema(description = "캠페인 신청 일자")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        val registerDate: LocalDateTime?,
        @Schema(description = "캠페인 이미지")
        val campaignImgUrl: String?,
        @Schema(description = "캠페인 제목")
        val campaignTitle: String?
    )
}

package tech.server.reviral.api.campaign.model.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.campaign.model.enums.CampaignPlatform
import tech.server.reviral.api.campaign.model.enums.CampaignStatus
import java.time.LocalDate
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : AdminCampaignsResponseDTO
 * author         : joy58
 * date           : 2025-01-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-20        joy58       최초 생성
 */
@Schema(name = "어드민 캠페인 목록 정보 조회")
data class AdminCampaignsResponseDTO(
    @Schema(description = "캠페인 일련번호")
    val campaignId: Long? = null,

    @Schema(description = "캠페인 상태 값")
    val campaignStatus: CampaignStatus? = CampaignStatus.WAIT,

    @Schema(description = "판매 시작일")
    val startDate: LocalDate = LocalDate.now(),

    @Schema(description = "판매 종료일")
    val finishDate: LocalDate = LocalDate.now(),

    @Schema(description = "플랫폼")
    val campaignPlatform: CampaignPlatform = CampaignPlatform.ETC,

    @Schema(description = "캠페인 명")
    val campaignTitle: String = "",

    @Schema(description = "총 가격")
    val campaignTotalPrice: Int = 0,

    @Schema(description = "모집인원")
    val totalRecruitCount: Int = 0,

    @Schema(description = "참여인원")
    val totalJoinCount: Long = 0,

    @Schema(description = "등록일")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime = LocalDateTime.now()
)

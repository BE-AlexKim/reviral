package tech.server.reviral.api.campaign.model.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.campaign.model.enums.CampaignPlatform
import tech.server.reviral.api.campaign.model.enums.EnrollStatus
import tech.server.reviral.api.campaign.model.enums.ImageStatus
import tech.server.reviral.api.campaign.model.enums.SellerStatus
import java.time.LocalDate
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : AdminCampaignDetailResponseDTO
 * author         : joy58
 * date           : 2025-01-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-21        joy58       최초 생성
 */
data class AdminCampaignDetailResponseDTO(
    val campaignId: Long? = 0,
    val campaignDetailsId: Long? = 0,
    val companyName: String? = "",
    val campaignTitle: String? = "",
    val startDate: LocalDate? = LocalDate.now(),
    val finishDate: LocalDate? = LocalDate.now(),
    val totalRecruitCount: Int? = 0,
    val applyDate: LocalDate? = LocalDate.now(),
    val sellerStatus: String? = "",
    val campaignPlatform: CampaignPlatform? = CampaignPlatform.ETC,
    val userInfo: MutableList<UserInfo>? = null
) {

    @Schema(name = "사용자 정보")
    data class UserInfo(
        @Schema(description = "캠페인 등록 일련번호")
        val campaignEnrollId: Long? = null,
        @Schema(description = "사용자 일련번호")
        val userId: Long? =null,
        @Schema(description = "등록 상태값")
        val enrollStatus: EnrollStatus? = EnrollStatus.APPLY,
        @Schema(description = "사용자 이름")
        val username: String? = null,
        @Schema(description = "플랫폼 아이디(네이버)")
        val nvId: String? = null,
        @Schema(description = "플랫폼 아이디(쿠팡)")
        val cpId: String? = null,
        @Schema(description = "휴대폰 번호")
        val phone: String? = null,
        @Schema(description = "주문 번호 이미지 링크")
        val orderUrl: String? = null,
        @Schema(description = "리뷰 이미지 링크")
        val reviewUrl: String? = null,
        @Schema(description = "리뷰 이미지 링크")
        val orderStatus: ImageStatus? = null,
        @Schema(description = "리뷰 이미지 링크")
        val reviewStatus: ImageStatus? = null,
        @Schema(description = "상태 변경일시")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
        val updateAt: LocalDateTime? = null
    )

}
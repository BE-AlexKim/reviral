package tech.server.reviral.api.campaign.model.dto

import com.fasterxml.jackson.annotation.JsonAlias
import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.campaign.model.enums.CampaignCategory
import tech.server.reviral.api.campaign.model.enums.CampaignPlatform
import tech.server.reviral.api.campaign.model.enums.OptionType
import java.time.LocalDate

/**
 *packageName    : tech.server.reviral.api.campaign.model.dto
 * fileName       : SaveCampaignRequestDTO
 * author         : joy58
 * date           : 2024-12-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-03        joy58       최초 생성
 */
@Schema(name = "캠페인 저장 응답 모델")
data class SaveCampaignRequestDTO(
    @Schema(description = "업체명", example = "몽테르", required = true)
    val companyName: String,

    @Schema(description = "캠페인 플랫폼", example = "NAVER, COUPANG, ETC", required = true)
    val platform: CampaignPlatform,

    @Schema(description = "캠페인 카테고리", example = "TIME, DAILY", required = true)
    val category: CampaignCategory,

    @Schema(description = "시간 구매 상품 시작 시간 값", example = "12:00", required = false)
    val startTime: String? = null,

    @Schema(description = "시간 구매 상품 종료 시간 값", example = "14:00", required = false)
    val endTime: String? = null,

    @Schema(description = "상품명", example = "칠초 무릎담요 극세사 캠핑 비행기 블랭킷", required = true)
    val productTitle: String,

    @Schema(description = "상품 이미지 링크", example = "https://naver.com", required = true)
    val campaignImgUrl: String,

    @Schema(description = "상품 링크", example = "https://naver.com", required = true)
    val campaignLink: String,

    @Schema(description = "상품 가격", example = "8900", required = true)
    val campaignPrice: Int,

    @Schema(description = "리뷰어 지급 포인트", example = "500", required = true)
    val reviewPoint: Int,

    @Schema(description = "캠페인 진행비용", example = "3000", required = true)
    val progressPrice: Int,

    @Schema(description = "판매 시작 날짜", example = "2024.12.31", required = true)
    val startSaleDateTime: LocalDate,

    @Schema(description = "판매 종료 날짜", example = "2025.01.31", required = true)
    val endSaleDateTime: LocalDate,

    @Schema(description = "옵션 타입", example = "SINGLE, MULTI", required = true)
    val optionType: OptionType,

    @Schema(description = "상품 옵션", required = false)
    val options: MutableList<Options>,

    @Schema(description = "셀러 모집글", required = true)
    val sellerRequest: String,

    @Schema(description = "쿠팡 와우회원 여부", required = false)
    val cpType: Boolean = false,

    @Schema(description = "주말 미포함 여부", required = false)
    val isNotWorkDay: Boolean = true,

//    @Schema(description = "구매 옵션1", required = true)
//    val firstOption: String,
//
//    @Schema(description = "구매 옵션2", required = false)
//    val secondOption: String? = null,

//    @Schema(description = "구매 수량", required = false)
//    @JsonAlias("count")
//    val paymentCount: String = "1",

    @Schema(description = "글자 수", required = false)
    val textLength: String? = null,

    @Schema(description = "강조 내용1", required = false)
    val firstStrengthText: String? = null,

    @Schema(description = "강조 내용2", required = false)
    val secondStrengthText: String? = null,

    @Schema(description = "강조 내용3", required = false)
    val thirdStrengthText: String? = null,

    @Schema(description = "강조 내용4", required = false)
    val forthStrengthText: String? = null,

    @Schema(description = "등록 조건", required = false)
    val condition: String? = null,

    @Schema(description = "기타 사항", required = false)
    val etcText: String? = null,

    ) {
    @Schema(name = "상품 옵션")
    data class Options(
        @Schema(description = "옵션명", example = "털모자")
        val optionTitle: String,
        @Schema(description = "총 모집 인원", example = "100")
        val recruitPeople: Int,
        @Schema(description = "하위 옵션")
        val subOption: MutableList<SubOptions>? = null
    )

    @Schema(name = "하위 옵션")
    data class SubOptions(
        @Schema(description = "하위 옵션명", example = "L 사이즈")
        val subOptionTitle: String,
        @Schema(description = "추가 금액", example = "5000")
        val addPrice: Int = 0,
        @Schema(description = "옵션 모집 인원", example = "100")
        val recruitPeople: Int
    )
}

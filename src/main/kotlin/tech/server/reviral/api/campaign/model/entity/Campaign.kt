package tech.server.reviral.api.campaign.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.Comment
import tech.server.reviral.api.campaign.model.enums.CampaignCategory
import tech.server.reviral.api.campaign.model.enums.CampaignPlatform
import tech.server.reviral.api.campaign.model.enums.CampaignStatus
import java.time.LocalDate
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.campaign.model.entity
 * fileName       : Campaign
 * author         : joy58
 * date           : 2024-11-19
 * description    : 캠페인
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@Entity
@Table(name = "tb_campaign_info")
data class Campaign(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id")
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_platform")
    @Comment("캠페인 플랫폼")
    var campaignPlatform: CampaignPlatform? = null,

    @Column(name = "campaign_title")
    @Comment("캠페인 제목")
    var campaignTitle: String? = null,

    @Column(name = "campaign_progress_price")
    var campaignProgressPrice: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_category")
    @Comment("캠페인 카테고리")
    var campaignCategory: CampaignCategory? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_status")
    @Comment("캠페인 상태")
    var campaignStatus: CampaignStatus? = null,

    @Column(name = "request_company")
    @Comment("업체명")
    var companyName: String? = null,

    @Column(name = "campaign_url")
    @Comment("캠페인 링크")
    var campaignUrl: String,

    @Column(name = "campaign_img_url")
    @Comment("캠페인 이미지 링크")
    var campaignImgUrl: String,

    @Column(name = "campaign_price")
    @Comment("캠페인 가격")
    var campaignPrice: Int,

    @Column(name = "campaign_total_price")
    @Comment("캠페인 총가격")
    var campaignTotalPrice: Int,

    @Column(name = "daily_recruit_count")
    @Comment("일별 모집인원")
    var dailyRecruitCount: Long,

    @Column(name = "total_recruit_count")
    @Comment("총 모집인원")
    var totalRecruitCount: Int,

    @Column(name = "option_count")
    var optionCount: Int,

    @Column(name = "start_time")
    @Comment("시간구매 시작 시간")
    var startTime: String? = null,

    @Column(name = "end_time")
    @Comment("시간구매 종료 시간")
    var endTime: String? = null,

    @Column(name = "review_point")
    @Comment("리뷰 가격")
    var reviewPoint: Int,

    @Column(name = "cp_type")
    @Comment("쿠팡 와우회원 여부")
    val cpType: Boolean = false,

    @Column(name = "active_date")
    @Comment("활성화 시간")
    var activeDate: LocalDate,

    @Column(name = "finish_date")
    @Comment("모집 마감 일시")
    var finishDate: LocalDate,

    @Column(name = "is_duplicated")
    @Comment("중복 가능여부")
    val isNotDuplicated: Boolean = false,

    @Column(name = "duplicated_date")
    val duplicatedDate: Long? = null,

    @Column(name = "is_workday")
    @Comment("주말 포함여부 true(미포함)/ false(포함)")
    val isNotWorkDay: Boolean = true,

    @Column(name = "seller_request", columnDefinition = "TEXT")
    @Comment("셀리 모집글")
    var sellerRequest: String,

//    @Column(name = "seller_guide", columnDefinition = "TEXT")
//    @Comment("구매 가이드")
//    var sellerGuide: String? = null,

    @Column(name = "create_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var updateAt: LocalDateTime? = null,


    // 연관관계 정의
    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var details: MutableList<CampaignDetails> = mutableListOf(),

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var options: MutableList<CampaignOptions> = mutableListOf(),

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var subOptions: MutableList<CampaignSubOptions>? = null,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "guide_id")
    var guide: CampaignGuide? = null

    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Campaign

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , campaignPlatform = $campaignPlatform , campaignTitle = $campaignTitle , campaignCategory = $campaignCategory , campaignStatus = $campaignStatus , companyName = $companyName , campaignUrl = $campaignUrl , campaignImgUrl = $campaignImgUrl , campaignPrice = $campaignPrice , campaignTotalPrice = $campaignTotalPrice , dailyRecruitCount = $dailyRecruitCount , totalCount = $totalRecruitCount , optionCount = $optionCount , startTime = $startTime , endTime = $endTime , reviewPoint = $reviewPoint , activeDate = $activeDate , finishDate = $finishDate , isDuplicated = $isNotDuplicated , duplicatedDate = $duplicatedDate , sellerRequest = $sellerRequest , createAt = $createAt , updateAt = $updateAt )"
    }
}

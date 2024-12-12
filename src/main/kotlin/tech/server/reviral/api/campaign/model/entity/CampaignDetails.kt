package tech.server.reviral.api.campaign.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.Comment
import java.time.LocalDate
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.campaign.model.entity
 * fileName       : CampanyDetails
 * author         : joy58
 * date           : 2024-12-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-03        joy58       최초 생성
 */
@Entity
@Table(name = "tb_campaign_details")
@Comment("캠페인 상세 정보 테이블")
data class CampaignDetails(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_details_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "campaign_id")
    val campaign: Campaign? = null,

    @Column(name = "campaign_url")
    var campaignUrl: String,

    @Column(name = "campaign_img_url")
    var campaignImgUrl: String,

    @Column(name = "campaign_price")
    var campaignPrice: Int,

    @Column(name = "campaign_total_price")
    val campaignTotalPrice: Int,

    @Column(name = "daily_count")
    val dailyRecruitCount: Long,

    @Column(name = "start_time")
    val startTime: String? = null,

    @Column(name = "end_time")
    val endTime: String? = null,

    @Column(name = "total_count")
    val totalCount: Int,

    @Column(name = "option_count")
    val optionCount: Int,

    @Column(name = "review_point")
    var reviewPoint: Int,

    @Column(name = "active_date")
    var activeDate: LocalDate,

    @Column(name = "finish_date")
    var finishDate: LocalDate,

    @Column(name = "active_count")
    val activeCount: Long,

    @Column(name = "seller_request", columnDefinition = "TEXT")
    val sellerRequest: String,

    @Column(name = "create_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val updateAt: LocalDateTime? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CampaignDetails

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , campaignUrl = $campaignUrl , campaignImgUrl = $campaignImgUrl , campaignPrice = $campaignPrice , campaignTotalPrice = $campaignTotalPrice , dailyCount = $dailyRecruitCount , startTime = $startTime , endTime = $endTime , totalCount = $totalCount , optionCount = $optionCount , reviewPoint = $reviewPoint , activeDate = $activeDate , finishDate = $finishDate , activeCount = $activeCount , sellerRequest = $sellerRequest , createAt = $createAt , updateAt = $updateAt )"
    }
}

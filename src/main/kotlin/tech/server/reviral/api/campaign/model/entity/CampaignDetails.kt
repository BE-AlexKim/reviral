package tech.server.reviral.api.campaign.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
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
@Table(name = "tb_campany_details")
data class CampaignDetails(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_details_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JsonIgnore
    @JoinColumn(name = "campaign_id")
    val campaign: Campaign,

    @Column(name = "campaign_url")
    val campaignUrl: String,

    @Column(name = "campaign_img_url")
    val campaignImgUrl: String,

    @Column(name = "campaign_price")
    val campaignPrice: Int,

    @Column(name = "campaign_total_price")
    val campaignTotalPrice: Int,

    @Column(name = "daily_count")
    val dailyCount: Int,

    @Column(name = "start_time")
    val startTime: String? = null,

    @Column(name = "end_time")
    val endTime: String? = null,

    @Column(name = "total_count")
    val totalCount: Int,

    @Column(name = "option_count")
    val optionCount: Int,

    @Column(name = "review_point")
    val reviewPoint: Int,

    @Column(name = "active_date")
    val activeDate: LocalDate,

    @Column(name = "finish_date")
    val finishDate: LocalDate,

    @Column(name = "active_count")
    val activeCount: Long,

    @Column(name = "seller_request")
    val sellerRequest: String,

    @Column(name = "create_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val updateAt: LocalDateTime? = null
)

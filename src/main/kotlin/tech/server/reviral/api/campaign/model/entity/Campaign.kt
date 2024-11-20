package tech.server.reviral.api.campaign.model.entity

import jakarta.persistence.*
import tech.server.reviral.api.campaign.model.enums.CampaignCategory
import tech.server.reviral.api.campaign.model.enums.CampaignStatus
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

    @Column(name = "campaign_status")
    @Enumerated(EnumType.STRING)
    val status: CampaignStatus,

    @Column(name = "campaign_category")
    @Enumerated(EnumType.STRING)
    val category: CampaignCategory,

    @Column(name = "campaign_platform")
    val platform: String,

    @Column(name = "campaign_title")
    val title: String,

    @Column(name = "campaign_img_url")
    val imgUrl: String,

    @Column(name = "campaign_link")
    val link: String,

    @Column(name = "campaign_price")
    val price: Int,

    @Column(name = "campaign_point")
    val point: Int,

    @Column(name = "recruit_count")
    val recruitCount: Int,

    @Column(name = "close_count")
    val closeCount: Int,

    @Column(name = "recruit_date")
    val recruitDate: LocalDateTime,

    @Column(name = "close_date")
    val closeDate: LocalDateTime,

    @Column(name = "open_date")
    val openDate: LocalDateTime,

    @Column(name = "create_at")
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_at")
    val updateAt: LocalDateTime? = null

)

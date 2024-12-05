package tech.server.reviral.api.campaign.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import tech.server.reviral.api.campaign.model.enums.CampaignCategory
import tech.server.reviral.api.campaign.model.enums.CampaignPlatform
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

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_platform")
    val campaignPlatform: CampaignPlatform,

    @Column(name = "campaign_title")
    val campaignTitle: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_category")
    val campaignCategory: CampaignCategory,

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_status")
    val campaignStatus: CampaignStatus,

    @Column(name = "request_company")
    val companyName: String,

    @Column(name = "create_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val updateAt: LocalDateTime? = null,

    @JsonIgnore
    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val details: MutableList<CampaignDetails> = mutableListOf()
)

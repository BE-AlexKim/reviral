package tech.server.reviral.api.campaign.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.Hibernate
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
    val campaignPlatform: CampaignPlatform? = null,

    @Column(name = "campaign_title")
    var campaignTitle: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_category")
    var campaignCategory: CampaignCategory? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "campaign_status")
    val campaignStatus: CampaignStatus? = null,

    @Column(name = "request_company")
    var companyName: String? = null,

    @Column(name = "create_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var updateAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var details: MutableList<CampaignDetails> = mutableListOf(),

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var options: MutableList<CampaignOptions> = mutableListOf(),

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var subOptions: MutableList<CampaignSubOptions> = mutableListOf(),


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
        return this::class.simpleName + "(id = $id , campaignPlatform = $campaignPlatform , campaignTitle = $campaignTitle , campaignCategory = $campaignCategory , campaignStatus = $campaignStatus , companyName = $companyName , createAt = $createAt , updateAt = $updateAt )"
    }
}

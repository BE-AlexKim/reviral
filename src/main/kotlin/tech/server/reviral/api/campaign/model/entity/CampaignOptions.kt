package tech.server.reviral.api.campaign.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.hibernate.Hibernate
import tech.server.reviral.api.campaign.model.enums.OptionType
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.campaign.model.entity
 * fileName       : CampaignOptions
 * author         : joy58
 * date           : 2024-12-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-03        joy58       최초 생성
 */
@Entity
@Table(name = "tb_campaign_options")
data class CampaignOptions(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_options_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "campaign_id")
    val campaign: Campaign? = null,

    @OneToMany(mappedBy="campaignOptions",fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val subOptions: List<CampaignSubOptions>? = null,

    @Column(name = "option_title")
    var title: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "option_type")
    var optionType: OptionType,

    @Column(name = "recruit_count")
    var recruitPeople: Int,

    @Column(name = "option_order")
    var order: Int,

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
        other as CampaignOptions

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , title = $title , optionType = $optionType , recruitPeople = $recruitPeople , order = $order , createAt = $createAt , updateAt = $updateAt )"
    }
}

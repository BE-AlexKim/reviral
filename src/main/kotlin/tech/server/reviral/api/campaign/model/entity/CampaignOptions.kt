package tech.server.reviral.api.campaign.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
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
    val campaign: Campaign,

    @Column(name = "option_title")
    val title: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "option_type")
    val optionType: OptionType,

    @Column(name = "recruit_count")
    val recruitPeople: Int,

    @Column(name = "option_order")
    val order: Int,

    @Column(name = "create_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val updateAt: LocalDateTime? = null
)

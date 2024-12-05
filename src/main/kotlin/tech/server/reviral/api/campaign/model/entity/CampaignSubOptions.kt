package tech.server.reviral.api.campaign.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.campaign.model.entity
 * fileName       : CampaignSubOptions
 * author         : joy58
 * date           : 2024-12-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-03        joy58       최초 생성
 */
@Schema(name = "캠페인 하위 옵션")
@Entity
@Table(name = "tb_campaign_sub_options")
data class CampaignSubOptions(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_sub_options_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JsonIgnore
    @JoinColumn(name = "campaign_options_id")
    val campaignOptions: CampaignOptions? = null,

    @Column(name = "option_title")
    val title: String,

    @Column(name = "option_count")
    val recruitPeople: Int,

    @Column(name = "add_price")
    val addPrice: Int,

    @Column(name = "option_order")
    val order: Int,

    @Column(name = "create_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val updateAt: LocalDateTime? = null

)

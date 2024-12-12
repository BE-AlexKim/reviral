package tech.server.reviral.api.campaign.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import org.hibernate.Hibernate
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
    @JoinColumn(name = "campaign_options_id")
    val campaignOptions: CampaignOptions? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "campaign_id")
    val campaign: Campaign? = null,

    @Column(name = "option_title")
    val title: String? = null,

    @Column(name = "option_count")
    val recruitPeople: Int? = null,

    @Column(name = "add_price")
    val addPrice: Int? = null,

    @Column(name = "option_order")
    val order: Int? = null,

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
        other as CampaignSubOptions

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , title = $title , recruitPeople = $recruitPeople , addPrice = $addPrice , order = $order , createAt = $createAt , updateAt = $updateAt )"
    }
}

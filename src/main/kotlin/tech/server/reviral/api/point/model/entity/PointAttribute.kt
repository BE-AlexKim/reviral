package tech.server.reviral.api.point.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.Comment
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.campaign.model.entity.CampaignEnroll
import tech.server.reviral.api.point.model.enums.PointStatus
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.point.model.entity
 * fileName       : PointHistory
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
@Entity
@Table(name = "tb_point_attribute")
@Comment("포인트 상태 정보 테이블")
data class PointAttribute(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_attr_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "campaign_enroll_id")
    val campaignEnroll: CampaignEnroll,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "point_status")
    var status: PointStatus,

    @Column(name = "point_value")
    val point: Int = 0,

    @Column(name = "create_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime? = null,

    @Column(name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var updateAt: LocalDateTime? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as PointAttribute

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , status = $status , point = $point , createAt = $createAt , updateAt = $updateAt )"
    }
}
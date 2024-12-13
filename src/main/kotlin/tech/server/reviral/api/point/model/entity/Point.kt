package tech.server.reviral.api.point.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.Comment
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.campaign.model.entity.CampaignEnroll
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.point.model.entity
 * fileName       : Point
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
@Entity
@Table(name = "tb_point_info")
data class Point(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    @Comment("포인트 일련번호")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "remain_point")
    @Comment("남은 포인트 잔액")
    val remainPoint: Int = 0,

    @Column(name = "expect_point")
    @Comment("예상 적립포인트")
    var expectPoint: Int = 0,

    @Column(name = "total_change_point")
    @Comment("총 전환 포인트 액수")
    val totalChangePoint: Int = 0,

    @Column(name = "create_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime? = null,

    @Column(name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val updateAt: LocalDateTime? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Point

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , remainPoint = $remainPoint , expectPoint = $expectPoint , totalChangePoint = $totalChangePoint , createAt = $createAt , updateAt = $updateAt )"
    }
}

package tech.server.reviral.api.point.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.Comment
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.point.model.enums.ExchangeStatus
import java.time.LocalDateTime
import java.util.UUID

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
@Table(name = "tb_point_exchange_info")
@Comment("포인트 전환 정보 테이블")
data class PointExchange(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_convert_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "exchange_status")
    @Comment("포인트 상태 값")
    var status: ExchangeStatus? = null,

    @Column(name = "point_value")
    @Comment("포인트 값")
    val pointValue: Int = 0,

    @Column(name = "exchange_desc")
    @Comment("포인트 설명 값")
    var exchangeDesc: String? = null,

    @Column(name = "exchange_unique_key", length = 14)
    @Comment("출금 고유값")
    var uniqueKey: String? = null,

    @Column(name = "is_download")
    @Comment("다계좌 다운로드 완료 여부")
    var downloadYn: Boolean = false,

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
        other as PointExchange

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , status = $status , pointValue = $pointValue , exchangeDesc = $exchangeDesc , createAt = $createAt , updateAt = $updateAt )"
    }
}

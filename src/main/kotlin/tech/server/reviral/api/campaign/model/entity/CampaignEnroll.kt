package tech.server.reviral.api.campaign.model.entity

import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.Comment
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.campaign.model.enums.EnrollStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 *packageName    : tech.server.reviral.api.campaign.model.entity
 * fileName       : CampaignJoin
 * author         : joy58
 * date           : 2024-12-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-11        joy58       최초 생성
 */
@Entity
@Table(name = "tb_campaign_enroll")
@Comment("캠페인 참여 정보 테이블")
data class CampaignEnroll(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_enroll_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "campaign_id")
    @Comment("캠페인 일련번호")
    val campaign: Campaign? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "campaign_options_id")
    @Comment("캠페인 옵션 일련번호")
    val options: CampaignOptions? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "campaign_sub_options_id")
    @Comment("캠페인 서브 옵션 일련번호")
    val subOptions: CampaignSubOptions? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "enroll_status")
    var enrollStatus: EnrollStatus? = null,

    @Column(name = "enroll_date")
    @Comment("참여 일자")
    val enrollDate: LocalDate = LocalDate.now(),

    @Column(name = "order_number")
    @Comment("상품 주문번호")
    var orderNo: String? = null,

    @Column(name = "review_img_url")
    @Comment("리뷰 캡처 이미지 링크")
    var reviewImgUrl: String? = null,

    @Column(name = "create_at")
    @Comment("최초 생성 일시")
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_at")
    @Comment("최종 수정 일시")
    var updateAt: LocalDateTime? = null

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CampaignEnroll

        return id != null && id == other.id
    }

    override fun hashCode(): Int = Objects.hash(id);

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(EmbeddedId = $id , enrollStatus = $enrollStatus , enrollDate = $enrollDate , createAt = $createAt , updateAt = $updateAt )"
    }
}
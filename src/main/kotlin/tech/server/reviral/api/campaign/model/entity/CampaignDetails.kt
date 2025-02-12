package tech.server.reviral.api.campaign.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.Comment
import tech.server.reviral.api.campaign.model.enums.SellerStatus
import java.time.LocalDate
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.campaign.model.entity
 * fileName       : CampanyDetails
 * author         : joy58
 * date           : 2024-12-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-03        joy58       최초 생성
 */
@Entity
@Table(name = "tb_campaign_details")
@Comment("캠페인 상세 정보 테이블")
data class CampaignDetails(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_details_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "campaign_id")
    val campaign: Campaign? = null,

    @Column(name = "campaign_date")
    @Comment("진행날짜")
    var applyDate: LocalDate,

    @Column(name = "campaigns_status")
    @Enumerated(EnumType.STRING)
    @Comment("캠페인 상태정보")
    var sellerStatus: SellerStatus,

    @Column(name = "join_count")
    @Comment("캠페인 시작인원")
    var joinCount: Long = 0,

    @Column(name = "is_delete")
    @Comment("삭제 유무")
    var isDelete: Boolean = false,

    @Column(name = "sort_no")
    @Comment("캠페인 순서번호")
    val sortNo: Int = 0,

    @Column(name = "recruit_count")
    @Comment("모집인원")
    var recruitCount: Long = 0,

    @Column(name = "create_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var updateAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "campaignDetails", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var enroll: MutableList<CampaignEnroll> = mutableListOf(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CampaignDetails

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , applyDate = $applyDate , campaignsStatus = $sellerStatus , joinCount = $joinCount , sortNo = $sortNo , recruitCount = $recruitCount , createAt = $createAt , updateAt = $updateAt )"
    }
}

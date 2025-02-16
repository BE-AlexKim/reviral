package tech.server.reviral.api.campaign.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.hibernate.Hibernate
import org.hibernate.annotations.Comment
import tech.server.reviral.api.campaign.model.enums.GuideType
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.bussiness.model.entity
 * fileName       : CampaignGuide
 * author         : joy58
 * date           : 2025-02-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-12        joy58       최초 생성
 */
@Entity
@Table(name = "tb_campaign_guide")
data class CampaignGuide(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guide_id")
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "guide_type")
    @Comment("가이드 타입")
    val guideType: GuideType? = null,

    @Comment("글자 수")
    @Column(name = "text_length")
    val textLength: String? = null,

    @Comment("강조 내용")
    @Column(name = "strength_text")
    val strengthText: String? = null,

    @Comment("등록 조건")
    @Column(name = "enroll_condition")
    val condition: String? = null,

    @Comment("기타 사항")
    @Column(name = "etc_text")
    val etcText: String? = null,

    @Column(name = "create_at")
    @Comment("생성 일시")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_at")
    @Comment("수정 일시")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val updateAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "campaign_id")
    val campaign: Campaign? = null

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CampaignGuide

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , guideType = $guideType , textLength = $textLength , strengthText = $strengthText , condition = $condition , etcText = $etcText , createAt = $createAt , updateAt = $updateAt )"
    }

}

package tech.server.reviral.api.bussiness.model.entity

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import tech.server.reviral.api.bussiness.model.enums.ImageCategory
import tech.server.reviral.api.bussiness.repository.PaymentGuide
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.bussiness.model.entity
 * fileName       : CampaignImages
 * author         : joy58
 * date           : 2025-02-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-03        joy58       최초 생성
 */
@Entity
@Table(name = "tb_image_info")
@Comment("이미지 테이블")
data class Image(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    @Comment("이미지 일련번호")
    val id: Long? = null,

    @Column(name = "image_category")
    @Enumerated(EnumType.STRING)
    @Comment("이미지 카테고리")
    val imageCategory: ImageCategory,

    @Column(name = "image_origin_name")
    @Comment("이미지 이름")
    val originName: String? = null,

    @Column(name = "image_name")
    @Comment("이미지 변환명")
    val imageName: String? = null,

    @Column(name = "image_url")
    @Comment("이미지 경로")
    val imageUrl: String? = null,

    @Column(name = "related_table")
    @Comment("연관 테이블 명")
    val tableName: String? = null,

    @Column(name = "related_id")
    @Comment("연관 테이블 컬럼 일련번호")
    val relatedId: Long? = null,

    @Column(name = "is_delete")
    @Comment("이미지 삭제여부")
    val deleteYn: Boolean = false,

    @Column(name = "create_at")
    @Comment("최초 생성 일시")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createAt: LocalDateTime,

    @Column(name = "update_at")
    @Comment("최종 수정 일시")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val updateAt: LocalDateTime? = null
)

package tech.server.reviral

import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.account.model.entity.QUser
import tech.server.reviral.api.campaign.model.dto.CampaignCardResponseDTO
import tech.server.reviral.api.campaign.model.dto.CampaignDetailResponseDTO
import tech.server.reviral.api.campaign.model.entity.QCampaign
import tech.server.reviral.api.campaign.model.entity.QCampaignDetails
import tech.server.reviral.api.campaign.model.entity.QCampaignEnroll
import tech.server.reviral.api.campaign.model.entity.QCampaignOptions
import tech.server.reviral.api.campaign.model.entity.QCampaignSubOptions
import tech.server.reviral.api.point.model.entity.QPointAttribute
import tech.server.reviral.api.point.model.entity.QPointExchange
import tech.server.reviral.api.point.model.enums.PointStatus
import tech.server.reviral.common.config.mail.EmailService
import java.time.LocalDate

/**
 *packageName    : tech.server.reviral
 * fileName       : JPAQuerydslTest
 * author         : joy58
 * date           : 2024-12-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-09        joy58       최초 생성
 */
@SpringBootTest
class JPAQuerydslTest(
) {
    @Autowired
    lateinit var queryFactory: JPAQueryFactory

    @Autowired
    lateinit var emailService: EmailService

    @DisplayName("JOIN TEST")
    @Test
    @Transactional
    fun findByCampaignTest() {
        val qCampaign = QCampaign.campaign
        val qCampaignDetails = QCampaignDetails.campaignDetails
        val qCampaignEnroll = QCampaignEnroll.campaignEnroll

        val query = queryFactory
            .select(
                Projections.fields(
                    CampaignCardResponseDTO::class.java,
                    qCampaign.id.`as`("campaignId"),
                    qCampaign.campaignTitle.`as`("campaignTitle"),
                    qCampaign.campaignStatus.`as`("campaignStatus"),
                    qCampaign.campaignPlatform.`as`("campaignPlatform"),
                    qCampaignDetails.campaignImgUrl.`as`("campaignImgUrl"),
                    qCampaignDetails.reviewPoint.`as`("campaignPoint"),
                    qCampaignDetails.totalCount.`as`("totalCount"),
                    qCampaignDetails.campaignPrice.`as`("campaignPrice"),
                    Expressions.numberTemplate(
                        Long::class.java,
                        "DATEDIFF({0}, {1})",
                        qCampaignDetails.finishDate,
                        LocalDate.now()
                    ).`as`("period"),
                    ExpressionUtils.`as`(
                        JPAExpressions
                            .select(qCampaignEnroll.count())
                            .from(qCampaignEnroll)
                            .where(
                                qCampaignEnroll.options.campaign.id.eq(qCampaign.id) // 동일한 campaignId 확인
                            ),
                        "joinCount"
                    )
                )
            )
            .from(qCampaign)
            .join(qCampaignDetails).on(qCampaignDetails.campaign.id.eq(qCampaign.id))
            .leftJoin(qCampaignEnroll).on(qCampaignEnroll.options.campaign.id.eq(qCampaign.id))
            .orderBy(
                Expressions.numberTemplate(
                    Long::class.java,
                    "DATEDIFF({0}, {1})",
                    qCampaignDetails.finishDate,
                    LocalDate.now()
                ).asc()
            )
            .fetch()


        println("QUERY ::::: $query")
    }

    @DisplayName("Query TEST")
    @Test
    @Transactional
    fun selectQuery() {
        val qCampaign = QCampaign.campaign
        val qCampaignDetails = QCampaignDetails.campaignDetails
        val qCampaignOptions = QCampaignOptions.campaignOptions
        val qCampaignSubOptions = QCampaignSubOptions.campaignSubOptions

        val query = queryFactory
            .select(
                Projections.constructor(
                    CampaignDetailResponseDTO::class.java,
                    qCampaignDetails.id.`as`("campaignDetailsId"),
                    qCampaign.campaignTitle.`as`("campaignTitle"),
                    qCampaign.campaignCategory.`as`("campaignCategory"),
                    qCampaignDetails.campaignUrl.`as`("campaignUrl"),
                    qCampaignDetails.campaignImgUrl.`as`("campaignImgUrl"),
                    qCampaignDetails.campaignPrice.`as`("campaignPrice"),
                    qCampaignDetails.reviewPoint.`as`("campaignPoint"),
                    qCampaignDetails.sellerRequest.`as`("sellerRequest"),
                    qCampaignDetails.totalCount.`as`("totalCount"),
                    Expressions.asNumber(0).`as`("joinCount"),
                    Projections.list(
                        Projections.constructor(
                            CampaignDetailResponseDTO.Options::class.java,
                            qCampaignOptions.id.`as`("campaignOptionsId"),
                            qCampaignOptions.title.`as`("optionTitle"),
                            Projections.list(
                                Projections.constructor(
                                    CampaignDetailResponseDTO.SubOptions::class.java,
                                    qCampaignSubOptions.id.`as`("campaignSubOptionsId"),
                                    qCampaignSubOptions.addPrice.`as`("campaignAddPrice"),
                                    qCampaignSubOptions.title.`as`("campaignSubOptionTitle"),
                                )
                            )
                        )
                    )
                )
            )
            .from(qCampaign)
            .join(qCampaignDetails).on(qCampaignDetails.campaign.id.eq(qCampaign.id))
            .join(qCampaignOptions).on(qCampaignOptions.campaign.id.eq(qCampaignDetails.campaign.id))
            .leftJoin(qCampaignSubOptions).on(qCampaignSubOptions.campaignOptions.id.eq(qCampaignOptions.id))
            .where(qCampaign.id.eq(17L))
            .distinct()
            .fetch()

        println("QUERY :::::: $query")

//        val group = query
//            .groupBy { it.campaignDetailsId }
//            .map { (_, campaigns) ->
//                val campaign = campaigns.first()
//
//                CampaignDetailResponseDTO(
//                    campaignDetailsId = campaign.campaignDetailsId,
//                    campaignTitle = campaign.campaignTitle,
//                    campaignCategory = campaign.campaignCategory,
//                    campaignUrl = campaign.campaignUrl,
//                    campaignImgUrl = campaign.campaignImgUrl,
//                    campaignPrice = campaign.campaignPrice,
//                    campaignPoint = campaign.campaignPoint,
//                    sellerRequest = campaign.sellerRequest,
//                    totalCount = campaign.totalCount,
//                    joinCount = campaign.joinCount,
//                    options = campaigns.flatMap { it.options }
//                        .groupBy { it!!.campaignOptionsId }
//                        .map { (_, options) ->
//                            val option = options.first()
//
//                            CampaignDetailResponseDTO.Options(
//                                campaignOptionsId = option!!.campaignOptionsId,
//                                optionTitle = option.optionTitle,
//                                subOptions = options.flatMap { it!!.subOptions }
//                                    .distinctBy { it?.campaignSubOptionsId }
//                            )
//                        }
//                )
//            }
//
//        println("GROUP :::: $group")
    }

    @Test
    @DisplayName("masking")
    fun mask() {
        val mask = "102010439392"

        val visual = mask.substring(0,mask.lastIndex-4)
        val masking = "*".repeat(5)

        println(visual+masking)
    }

    @DisplayName("Point")
    @Test
    @Transactional
    fun pointQuery() {

        val qUser = QUser.user
        val qPointAttribute = QPointAttribute.pointAttribute
        val qPointExchange = QPointExchange.pointExchange

        val query = queryFactory
            .from(qPointExchange)
            .leftJoin(qPointExchange.user, qUser)
            .on(qPointExchange.user.id.eq(qUser.id))
            .leftJoin(qPointAttribute.user, qUser)
            .on(qPointAttribute.user.id.eq(qUser.id))
            .where(
                qUser.id.eq(1)
                    .and(qPointAttribute.status.eq(PointStatus.COMPLETE))
            )
            .orderBy(
                qPointExchange.createAt.desc().nullsLast(),
                qPointAttribute.createAt.desc().nullsLast()
            ).fetch()

        println("QUERY ::::: $query")
    }

    @Test
    @DisplayName("Send Email")
    fun send() {

        val send = emailService.send(
            "joy585@naver.com",
            "[리바이럴] 회원가입 인증을 위한 인증번호 안내",
            "auth",
            hashMapOf("code" to "12345","username" to "joy585@naver.com")
        )

    }
}
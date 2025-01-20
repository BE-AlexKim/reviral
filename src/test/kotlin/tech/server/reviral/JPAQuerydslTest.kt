package tech.server.reviral

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.account.model.entity.QUser
import tech.server.reviral.api.account.repository.AccountRepository
import tech.server.reviral.api.account.service.AccountService
import tech.server.reviral.api.campaign.model.dto.CampaignCardResponseDTO
import tech.server.reviral.api.campaign.model.dto.CampaignDetailResponseDTO
import tech.server.reviral.api.campaign.model.entity.QCampaign
import tech.server.reviral.api.campaign.model.entity.QCampaignDetails
import tech.server.reviral.api.campaign.model.entity.QCampaignEnroll
import tech.server.reviral.api.campaign.model.entity.QCampaignOptions
import tech.server.reviral.api.campaign.model.entity.QCampaignSubOptions
import tech.server.reviral.api.account.model.enums.BankCode
import tech.server.reviral.api.campaign.model.dto.AdminCampaignDetailsResponseDTO
import tech.server.reviral.api.campaign.model.dto.AdminCampaignsResponseDTO
import tech.server.reviral.api.campaign.model.enums.CampaignStatus
import tech.server.reviral.api.campaign.model.enums.SellerStatus
import tech.server.reviral.api.campaign.repository.CampaignDetailsRepository
import tech.server.reviral.api.point.model.entity.QPointAttribute
import tech.server.reviral.api.point.model.entity.QPointExchange
import tech.server.reviral.api.point.model.enums.PointStatus
import tech.server.reviral.common.config.mail.EmailService
import tech.server.reviral.common.config.response.exception.CampaignException
import java.time.LocalDate
import java.time.LocalDateTime

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
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var emailService: EmailService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var campaignDetailsRepository: CampaignDetailsRepository

    @DisplayName("Point")
    @Test
    @Transactional
    fun pointQuery() {

        val qCampaign = QCampaign.campaign
        val qCampaignDetails = QCampaignDetails.campaignDetails
        val qCampaignOptions = QCampaignOptions.campaignOptions
        val qCampaignSubOptions = QCampaignSubOptions.campaignSubOptions

        val query = queryFactory
            .select(
                Projections.constructor(
                    CampaignDetailResponseDTO::class.java,
                    qCampaign.id.`as`("campaignId"),
                    qCampaign.campaignTitle.`as`("campaignTitle"),
                    qCampaign.campaignCategory.`as`("campaignCategory"),
                    qCampaign.campaignUrl.`as`("campaignUrl"),
                    qCampaign.campaignImgUrl.`as`("campaignImgUrl"),
                    qCampaign.campaignPrice.`as`("campaignPrice"),
                    qCampaign.reviewPoint.`as`("campaignPoint"),
                    qCampaign.sellerRequest.`as`("sellerRequest"),
                    qCampaignDetails.recruitCount.`as`("totalCount"),
                    qCampaignDetails.joinCount.`as`("joinCount"),
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
            .join(qCampaignOptions).on(qCampaignOptions.campaign.id.eq(qCampaign.id))
            .leftJoin(qCampaignSubOptions).on(qCampaignSubOptions.campaignOptions.id.eq(qCampaignOptions.id))
            .where(
                qCampaign.id.eq(1)
                    .and(qCampaignDetails.sellerStatus.eq(SellerStatus.ACTIVE))
            )
            .groupBy(
                qCampaignDetails.id,
                qCampaign.id,
                qCampaignOptions.id,
                qCampaignSubOptions.id
            )
            .distinct()
            .fetch()

        println("QUERY ::::: $query")
    }

    @Test
    @DisplayName("JPA Transactional TEST")
    fun transaction() {
    }

    @Test
    @DisplayName("Send Email")
    fun send() {

        val campaignId = 1L

        val qCampaign = QCampaign.campaign
        val qCampaignDetails = QCampaignDetails.campaignDetails

        val query = queryFactory
            .select(
                Projections.constructor(
                    AdminCampaignDetailsResponseDTO::class.java,
                    qCampaignDetails.id.`as`("campaignDetailId"),
                    qCampaignDetails.sellerStatus.`as`("sellerStatus"),
                    qCampaignDetails.applyDate.`as`("applyDate"),
                    qCampaign.campaignTitle.`as`("campaignTitle"),
                    Expressions.numberTemplate(
                        Long::class.java,
                        "({0} * {1}) + ({2} * {1})",
                        qCampaign.campaignPrice,
                        qCampaignDetails.recruitCount,
                        qCampaign.campaignProgressPrice
                    ).`as`("campaignPrice")
                )
            )
            .from(qCampaign)
            .join(qCampaignDetails).on(qCampaign.eq(qCampaignDetails.campaign))
            .where(qCampaignDetails.campaign.id.eq(campaignId))

        println(query.fetch())

    }

    @DisplayName("select kind of campaign status")
    @Test
    fun status() {

        val campaign = QCampaign.campaign
        val detail = QCampaignDetails.campaignDetails
        val enroll = QCampaignEnroll.campaignEnroll

        val query = queryFactory
            .select(campaign, detail, enroll)
            .from(campaign)
            .join(detail)
            .on(campaign.id.eq(detail.campaign.id))
            .leftJoin(enroll)
            .on(campaign.id.eq(enroll.campaign.id))
            .fetch()

        println("QUERY ::::: $query")
        println("COUNT ::::: ${query.size}")

    }

    @DisplayName("PASS")
    @Test
    fun password() {
        val bankCode: String? = null

        val code = BankCode.values().first { it.getBankCode() == bankCode }.getBankName()

        println("Bank CODE :::: $code")
    }
}
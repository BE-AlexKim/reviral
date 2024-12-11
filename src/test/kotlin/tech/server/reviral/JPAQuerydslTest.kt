package tech.server.reviral

import com.querydsl.core.group.GroupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.campaign.model.dto.CampaignDetailResponseDTO
import tech.server.reviral.api.campaign.model.entity.QCampaign
import tech.server.reviral.api.campaign.model.entity.QCampaignDetails
import tech.server.reviral.api.campaign.model.entity.QCampaignOptions
import tech.server.reviral.api.campaign.model.entity.QCampaignSubOptions
import java.time.LocalDate
import java.util.Locale

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

    @DisplayName("JOIN TEST")
    @Test
    @Transactional
    fun findByCampaignTest() {
        val qTableCampaign = QCampaign.campaign
        val qTableCampaignDetails = QCampaignDetails.campaignDetails

        val join = queryFactory
            .select(qTableCampaign.campaignTitle,qTableCampaignDetails)
            .from(qTableCampaign)
            .join(qTableCampaignDetails).on(qTableCampaignDetails.campaign.id.eq(qTableCampaign.id))
            .where(qTableCampaignDetails.activeDate.before(LocalDate.now()))
            .where(qTableCampaignDetails.finishDate.after(LocalDate.now()))
            .fetch()
        println(join)
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
            .join(qCampaignSubOptions).on(qCampaignSubOptions.campaignOptions.id.eq(qCampaignOptions.id))
            .where(qCampaign.id.eq(17L))
            .groupBy(
                qCampaignDetails.id,
                qCampaign.id,
                qCampaignOptions.id,
                qCampaignSubOptions.id
            )
            .fetch()

        val group = query
            .groupBy { it.campaignDetailsId }
            .map { (_, campaigns) ->
                val campaign = campaigns.first()

                CampaignDetailResponseDTO(
                    campaignDetailsId = campaign.campaignDetailsId,
                    campaignTitle = campaign.campaignTitle,
                    campaignCategory = campaign.campaignCategory,
                    campaignUrl = campaign.campaignUrl,
                    campaignImgUrl = campaign.campaignImgUrl,
                    campaignPrice = campaign.campaignPrice,
                    campaignPoint = campaign.campaignPoint,
                    sellerRequest = campaign.sellerRequest,
                    totalCount = campaign.totalCount,
                    joinCount = campaign.joinCount,
                    options = campaigns.flatMap { it.options }
                        .groupBy { it!!.campaignOptionsId }
                        .map { (_, options) ->
                            val option = options.first()

                            CampaignDetailResponseDTO.Options(
                                campaignOptionsId = option!!.campaignOptionsId,
                                optionTitle = option.optionTitle,
                                subOptions = options.flatMap { it!!.subOptions }
                                    .distinctBy { it?.campaignSubOptionsId }
                            )
                        }
                )
            }

        println("GROUP :::: $group")
    }
}
package tech.server.reviral.api.campaign.service

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.account.model.dto.SignInRequestDTO
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.account.model.enums.UserRole
import tech.server.reviral.api.campaign.model.dto.AdminCampaignDetailsResponseDTO
import tech.server.reviral.api.campaign.model.dto.AdminCampaignsResponseDTO
import tech.server.reviral.api.campaign.model.entity.QCampaign
import tech.server.reviral.api.campaign.model.entity.QCampaignDetails
import tech.server.reviral.api.campaign.model.enums.CampaignPlatform
import tech.server.reviral.api.campaign.model.enums.CampaignStatus
import tech.server.reviral.common.config.response.exception.BasicException
import tech.server.reviral.common.config.response.exception.enums.BasicError
import tech.server.reviral.common.config.security.JwtToken
import tech.server.reviral.common.config.security.JwtTokenProvider

/**
 *packageName    : tech.server.reviral.api.campaign.service
 * fileName       : CampaignAdminService
 * author         : joy58
 * date           : 2025-01-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-14        joy58       최초 생성
 */
@Service
class CampaignAdminService constructor(
    private val queryFactory: JPAQueryFactory,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    /**
     *  어드민 목록 조회
     */
    @Transactional
    fun getCampaigns(status: String?, platform: String?, campaignTitle: String?, pageable: Pageable): MutableList<AdminCampaignsResponseDTO> {
        val qCampaign = QCampaign.campaign
        val qCampaignDetails = QCampaignDetails.campaignDetails

        val query = queryFactory
            .select(
                Projections.constructor(
                    AdminCampaignsResponseDTO::class.java,
                    qCampaign.id.`as`("campaignId"),
                    qCampaign.campaignStatus.`as`("campaignStatus"),
                    qCampaign.activeDate.`as`("startDate"),
                    qCampaign.finishDate.`as`("finishDate"),
                    qCampaign.campaignPlatform.`as`("campaignPlatform"),
                    qCampaign.campaignTitle.`as`("campaignTitle"),
                    qCampaign.campaignTotalPrice.`as`("campaignTotalPrice"),
                    qCampaign.totalRecruitCount.`as`("totalRecruitCount"),
                    qCampaignDetails.joinCount.sum().`as`("totalJoinCount"),
                    qCampaign.createAt.`as`("createAt")
                )
            )
            .from(qCampaign)
            .join(qCampaignDetails).on(qCampaign.eq(qCampaignDetails.campaign))
            .groupBy(qCampaign.id)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        val booleanBuilder = BooleanBuilder()

        if (campaignTitle != null) {
            booleanBuilder.and(
                qCampaign.campaignTitle.like("%${campaignTitle}%")
            )
        }

        if (status != null) {
            booleanBuilder.and(
                qCampaign.campaignStatus.eq(CampaignStatus.valueOf(status.uppercase()))
            )
        }

        if (platform != null) {
            booleanBuilder.and(
                qCampaign.campaignPlatform.eq(CampaignPlatform.valueOf(platform.uppercase()))
            )
        }

        return query.fetch()
    }

    @Transactional
    fun getCampaignDetails( campaignId: Long ): MutableList<AdminCampaignDetailsResponseDTO> {

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

        return query.fetch()
    }
}
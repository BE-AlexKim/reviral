package tech.server.reviral.api.campaign.service

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.Tuple
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.campaign.model.dto.CampaignCardResponseDTO
import tech.server.reviral.api.campaign.model.dto.CampaignDetailResponseDTO
import tech.server.reviral.api.campaign.model.dto.SaveCampaignRequestDTO
import tech.server.reviral.api.campaign.model.entity.Campaign
import tech.server.reviral.api.campaign.model.entity.CampaignDetails
import tech.server.reviral.api.campaign.model.entity.CampaignOptions
import tech.server.reviral.api.campaign.model.entity.CampaignSubOptions
import tech.server.reviral.api.campaign.model.entity.QCampaign
import tech.server.reviral.api.campaign.model.entity.QCampaignDetails
import tech.server.reviral.api.campaign.model.entity.QCampaignOptions
import tech.server.reviral.api.campaign.model.entity.QCampaignSubOptions
import tech.server.reviral.api.campaign.model.enums.CampaignCategory
import tech.server.reviral.api.campaign.model.enums.CampaignPlatform
import tech.server.reviral.api.campaign.model.enums.CampaignStatus
import tech.server.reviral.api.campaign.model.enums.OptionType
import tech.server.reviral.api.campaign.repository.CampaignDetailsRepository
import tech.server.reviral.api.campaign.repository.CampaignOptionsRepository
import tech.server.reviral.api.campaign.repository.CampaignRepository
import tech.server.reviral.api.campaign.repository.CampaignSubOptionsRepository
import tech.server.reviral.common.config.response.exception.CampaignException
import tech.server.reviral.common.config.response.exception.enums.CampaignError
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 *packageName    : tech.server.reviral.api.campaign.service
 * fileName       : CampaignService
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@Service
class CampaignService constructor(
    private val campaignRepository: CampaignRepository,
    private val campaignDetailsRepository: CampaignDetailsRepository,
    private val campaignOptionsRepository: CampaignOptionsRepository,
    private val campaignSubOptionsRepository: CampaignSubOptionsRepository,
    private val queryFactory: JPAQueryFactory
){


    /**
     * 구매 캠페인 정보 조회
     * @param category: String?
     * @param platform: String?
     * @param status: String?
     * @param offset: Long?,
     * @param limit: Long?
     * @return List<CampaignCardResponseDTO>
     */
    @Transactional
    @Throws(CampaignException::class)
    fun searchCampaigns(
        category: String?,
        platform: String?,
        status: String?,
        offset: Long?,
        limit: Long?
    ): List<CampaignCardResponseDTO> {

        val qCampaign = QCampaign.campaign
        val qCampaignDetails = QCampaignDetails.campaignDetails

        val booleanBuilder = BooleanBuilder()

        // 추가 조건
        if (status != null) { // 카테고리
            when(status) {
                "wait" -> { booleanBuilder.and(qCampaign.campaignStatus.eq(CampaignStatus.WAIT))}
                "progress" -> {
                    booleanBuilder.and(qCampaign.campaignStatus.eq(CampaignStatus.PROGRESS))
                    booleanBuilder.and(qCampaignDetails.activeDate.before(LocalDate.now()))
                    booleanBuilder.and(qCampaignDetails.finishDate.after(LocalDate.now()))
                }
                "finish" -> { booleanBuilder.and(qCampaign.campaignStatus.eq(CampaignStatus.FINISH))}
                "recruitment" -> { booleanBuilder.and(qCampaign.campaignStatus.eq(CampaignStatus.RECRUITMENT))}
            }
        }

        if (platform != null) { // 플랫폼
            when(platform) {
                "nv" -> {
                    booleanBuilder.and(qCampaign.campaignPlatform.eq(CampaignPlatform.NAVER))
                    booleanBuilder.and(qCampaignDetails.activeDate.before(LocalDate.now()))
                    booleanBuilder.and(qCampaignDetails.finishDate.after(LocalDate.now()))
                }
                "cp" -> {
                    booleanBuilder.and(qCampaign.campaignPlatform.eq(CampaignPlatform.COUPANG))
                    booleanBuilder.and(qCampaignDetails.activeDate.before(LocalDate.now()))
                    booleanBuilder.and(qCampaignDetails.finishDate.after(LocalDate.now()))
                }
                "et" -> {
                    booleanBuilder.and(qCampaign.campaignPlatform.eq(CampaignPlatform.ETC))
                    booleanBuilder.and(qCampaignDetails.activeDate.before(LocalDate.now()))
                    booleanBuilder.and(qCampaignDetails.finishDate.after(LocalDate.now()))
                }
            }
        }

        if (category != null) {
            when(category) {
                "time" -> {
                    booleanBuilder.and(qCampaign.campaignCategory.eq(CampaignCategory.TIME))
                }
                "daily" -> {
                    booleanBuilder.and(qCampaign.campaignCategory.eq(CampaignCategory.DAILY))
                }
                "deadline" -> {
                    booleanBuilder.and(qCampaignDetails.finishDate.eq(LocalDate.now()))
                }
                "today" -> {
                    booleanBuilder.and(qCampaignDetails.activeDate.eq(LocalDate.now()))
                }
            }
        }

        // 순차정렬 enum 커스텀
        val order = CaseBuilder()
            .`when`(qCampaign.campaignStatus.eq(CampaignStatus.PROGRESS)).then(0)
            .`when`(qCampaign.campaignStatus.eq(CampaignStatus.WAIT)).then(1)
            .`when`(qCampaign.campaignStatus.eq(CampaignStatus.FINISH)).then(2)
            .otherwise(3)

        return queryFactory
            .select(
                Projections.fields(
                    CampaignCardResponseDTO::class.java,
                    qCampaign.id.`as`("campaignId"),
                    qCampaign.campaignTitle.`as`("campaignTitle"),
                    qCampaign.campaignPlatform.`as`("campaignPlatform"),
                    qCampaignDetails.campaignImgUrl.`as`("campaignImgUrl"),
                    qCampaignDetails.reviewPoint.`as`("campaignPoint"),
                    qCampaignDetails.totalCount.`as`("totalCount"),
                    qCampaignDetails.campaignPrice.`as`("campaignPrice"),
                    qCampaign.campaignStatus.`as`("campaignStatus")
                )
            )
            .from(qCampaign)
            .join(qCampaignDetails).on(qCampaignDetails.campaign.id.eq(qCampaign.id))
            .where(booleanBuilder)
            .orderBy(qCampaign.createAt.desc())
            .offset(offset ?: 0)
            .limit(limit ?: 10)
            .orderBy(order.asc())
            .fetch()

    }

    /**
     * 당일구매 캠페인 목록 정보 조회
     * @param campaignId: Long
     */
    @Transactional
    @Throws(CampaignException::class)
    fun getCampaign(campaignId: Long): List<CampaignDetailResponseDTO> {

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
            .where(qCampaign.id.eq(campaignId))
            .groupBy(
                qCampaignDetails.id,
                qCampaign.id,
                qCampaignOptions.id,
                qCampaignSubOptions.id
            )
            .fetch()

        return query
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
    }

    /**
     * 캠페인 정보 등록
     */
    @Transactional
    @Throws(CampaignException::class)
    fun setCampaign( request: SaveCampaignRequestDTO ): Boolean {

        // 캠페인 정보 등록
        val campaign = campaignRepository.save(Campaign(
            companyName = request.companyName,
            campaignPlatform = request.platform,
            campaignTitle = request.productTitle,
            campaignStatus = CampaignStatus.WAIT,
            campaignCategory = request.category
        ))

        var totalCount = 0

        // 캠페인 옵션 정보 등록
        if ( request.optionType == OptionType.SINGLE ) { // 단독형 옵션
            request.options.forEachIndexed { index,option ->
                campaignOptionsRepository.save(CampaignOptions(
                    campaign = campaign,
                    title = option.optionTitle,
                    optionType = request.optionType,
                    order = index,
                    recruitPeople = option.recruitPeople
                ))
                totalCount = option.recruitPeople
            }
        }else { // 조합형 옵션
            request.options.forEachIndexed { index,option ->
                val options = campaignOptionsRepository.save(campaignOptionsRepository.save(CampaignOptions(
                    campaign = campaign,
                    title = option.optionTitle,
                    optionType = request.optionType,
                    order = index,
                    recruitPeople = option.subOption?.sumOf { it.recruitPeople } ?: 0
                )))

                option.subOption!!.forEachIndexed { subIndex, subOption ->
                    campaignSubOptionsRepository.save(
                        CampaignSubOptions(
                            campaignOptions = options,
                            title = subOption.subOptionTitle,
                            order = subIndex,
                            addPrice = subOption.addPrice,
                            recruitPeople = subOption.recruitPeople,
                        )
                    )
                    totalCount += subOption.recruitPeople
                }
            }
        }

        val activeCount = getLocalDateBetween(request.startSaleDateTime, request.endSaleDateTime)

        campaignDetailsRepository.save(
            CampaignDetails(
                campaign = campaign,
                campaignUrl = request.campaignLink,
                campaignImgUrl = request.campaignImgUrl,
                campaignPrice = request.campaignPrice,
                campaignTotalPrice = request.campaignPrice * request.options.sumOf { it.recruitPeople },
                optionCount = request.options.size,
                reviewPoint = request.reviewPoint,
                totalCount = totalCount,
                activeDate = request.startSaleDateTime,
                finishDate = request.endSaleDateTime,
                activeCount = activeCount,
                sellerRequest = request.sellerRequest,
                dailyCount = totalCount / activeCount.toInt(),
                startTime = request.startTime,
                endTime = request.endTime
            )
        )
        return true
    }

    @Throws(CampaignException::class)
    private fun getLocalDateBetween(startDate: LocalDate, endDate: LocalDate): Long {
        if ( startDate.isBefore(endDate) ) {
            return ChronoUnit.DAYS.between(startDate,endDate)
        }else {
            throw CampaignException(CampaignError.START_DATE_SET)
        }
    }

}
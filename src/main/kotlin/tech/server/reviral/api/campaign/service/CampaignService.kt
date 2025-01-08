package tech.server.reviral.api.campaign.service

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import tech.server.reviral.api.account.repository.AccountRepository
import tech.server.reviral.api.campaign.model.dto.*
import tech.server.reviral.api.campaign.model.entity.*
import tech.server.reviral.api.campaign.model.enums.*
import tech.server.reviral.api.campaign.repository.*
import tech.server.reviral.api.point.model.entity.Point
import tech.server.reviral.api.point.model.entity.PointAttribute
import tech.server.reviral.api.point.model.entity.QPointAttribute
import tech.server.reviral.api.point.model.enums.PointStatus
import tech.server.reviral.api.point.repository.PointAttributeRepository
import tech.server.reviral.api.point.repository.PointRepository
import tech.server.reviral.common.config.aws.AmazonS3Service
import tech.server.reviral.common.config.response.exception.BasicException
import tech.server.reviral.common.config.response.exception.CampaignException
import tech.server.reviral.common.config.response.exception.PointException
import tech.server.reviral.common.config.response.exception.enums.BasicError
import tech.server.reviral.common.config.response.exception.enums.CampaignError
import java.time.LocalDate
import java.time.LocalDateTime
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
    private val accountRepository: AccountRepository,
    private val campaignEnrollRepository: CampaignEnrollRepository,
    private val pointRepository: PointRepository,
    private val pointAttributeRepository: PointAttributeRepository,
    private val queryFactory: JPAQueryFactory,
    private val amazonS3Service: AmazonS3Service
){


    /**
     * 구매 캠페인 정보 조회
     * @param category: String?
     * @param platform: String?
     * @param status: String?
     * @param pageable: Pageable
     * @return List<CampaignCardResponseDTO>
     */
    @Transactional
    @Throws(CampaignException::class)
    fun searchCampaigns(
        category: String?,
        platform: String?,
        status: String?,
        pageable: Pageable
    ): List<CampaignCardResponseDTO> {

        val qCampaign = QCampaign.campaign
        val qCampaignDetails = QCampaignDetails.campaignDetails
        val qCampaignEnroll = QCampaignEnroll.campaignEnroll

        val now = LocalDate.now()

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
                        Expressions.constant(LocalDate.now())
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
            .distinct()

        val booleanBuilder = BooleanBuilder()

        // 추가 조건
        if (status != null) { // 카테고리
            when(status) {
                "wait" -> {
                    booleanBuilder.and(qCampaign.campaignStatus.eq(CampaignStatus.WAIT))
                    booleanBuilder.and(qCampaignDetails.activeDate.before(now))
                    booleanBuilder.and(qCampaignDetails.finishDate.after(now))
                }
                "progress" -> {
                    booleanBuilder.and(qCampaign.campaignStatus.eq(CampaignStatus.PROGRESS))
                    booleanBuilder.and(qCampaignDetails.activeDate.before(now))
                    booleanBuilder.and(qCampaignDetails.finishDate.after(now))
                }
                "finish" -> {
                    booleanBuilder.and(qCampaign.campaignStatus.eq(CampaignStatus.FINISH))
                }
                "recruitment" -> {
                    booleanBuilder.and(qCampaign.campaignStatus.eq(CampaignStatus.RECRUITMENT))
                    booleanBuilder.and(qCampaignDetails.activeDate.before(now))
                    booleanBuilder.and(qCampaignDetails.finishDate.after(now))
                }
            }
        }

        if (platform != null) { // 플랫폼
            when(platform) {
                "nv" -> {
                    booleanBuilder.and(qCampaign.campaignPlatform.eq(CampaignPlatform.NAVER))
                }
                "cp" -> {
                    booleanBuilder.and(qCampaign.campaignPlatform.eq(CampaignPlatform.COUPANG))
                }
                "et" -> {
                    booleanBuilder.and(qCampaign.campaignPlatform.eq(CampaignPlatform.ETC))
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
                    booleanBuilder.and(
                        qCampaignDetails.activeDate.loe(now)
                            .and(qCampaignDetails.finishDate.goe(now))
                    )

                    return query
                        .where(booleanBuilder)
                        .offset(pageable.offset)
                        .limit(pageable.pageSize.toLong())
                        .orderBy(
                            Expressions.numberTemplate(
                                Long::class.java,
                                "DATEDIFF({0}, {1})",
                                qCampaignDetails.finishDate,
                                Expressions.constant(now)
                            ).asc()
                        )
                        .fetch()
                }
                "today" -> {
                    booleanBuilder.and(qCampaignDetails.activeDate.eq(now))
                }

                "best" -> {

                    booleanBuilder.and(
                        qCampaignDetails.activeDate.loe(now)
                            .and(qCampaignDetails.finishDate.goe(now))
                    )

                    return query
                        .where(booleanBuilder)
                        .limit(5)
                        .orderBy( // 참여인원이 많은 순서
                            Expressions.numberTemplate(
                                Long::class.java,
                                "{0}",
                                JPAExpressions
                                    .select(qCampaignEnroll.count())
                                    .from(qCampaignEnroll)
                                    .where(
                                        qCampaignEnroll.options.campaign.id.eq(qCampaign.id) // 동일한 campaignId 확인
                                    )
                            ).desc().nullsLast(),
                            Expressions.numberTemplate( // 마감일 기준
                                Long::class.java,
                                "DATEDIFF({0}, {1})",
                                qCampaignDetails.finishDate,
                                Expressions.constant(LocalDate.now())
                            ).asc()
                        )
                        .distinct()
                        .fetch()
                }
            }
        }

        if ( status != "finish" ) {
            booleanBuilder.and(
                qCampaignDetails.activeDate.loe(now)
                    .and(qCampaignDetails.finishDate.goe(now))
            )
        }

        // 순차정렬 enum 커스텀
        val order = CaseBuilder()
            .`when`(qCampaign.campaignStatus.eq(CampaignStatus.PROGRESS)).then(0)
            .`when`(qCampaign.campaignStatus.eq(CampaignStatus.WAIT)).then(1)
            .`when`(qCampaign.campaignStatus.eq(CampaignStatus.FINISH)).then(2)
            .otherwise(3)

        return query
            .where(booleanBuilder)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(order.asc())
            .fetch()
    }

    /**
     * 캠페인 목록 정보 조회
     * @param campaignId: Long
     */
    @Transactional
    @Throws(CampaignException::class)
    fun getCampaign(campaignId: Long): CampaignDetailResponseDTO {

        val qCampaign = QCampaign.campaign
        val qCampaignDetails = QCampaignDetails.campaignDetails
        val qCampaignOptions = QCampaignOptions.campaignOptions
        val qCampaignSubOptions = QCampaignSubOptions.campaignSubOptions
        val qCampaignEnroll = QCampaignEnroll.campaignEnroll

        val query = queryFactory
            .select(
                Projections.constructor(
                    CampaignDetailResponseDTO::class.java,
                    qCampaign.id.`as`("campaignId"),
                    qCampaign.campaignTitle.`as`("campaignTitle"),
                    qCampaign.campaignCategory.`as`("campaignCategory"),
                    qCampaignDetails.campaignUrl.`as`("campaignUrl"),
                    qCampaignDetails.campaignImgUrl.`as`("campaignImgUrl"),
                    qCampaignDetails.campaignPrice.`as`("campaignPrice"),
                    qCampaignDetails.reviewPoint.`as`("campaignPoint"),
                    qCampaignDetails.sellerRequest.`as`("sellerRequest"),
                    qCampaignDetails.totalCount.`as`("totalCount"),
                    ExpressionUtils.`as`(
                        JPAExpressions
                            .select(qCampaignEnroll.count())
                            .from(qCampaignEnroll)
                            .where(
                                qCampaignEnroll.options.campaign.id.eq(qCampaign.id) // 동일한 campaignId 확인
                            ),
                        "joinCount"
                    ),
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
            .distinct()
            .fetch()

        val groupBy = query.groupBy { it.campaignId }
            .map { (_, campaigns) ->
                val campaign = campaigns.first()

                CampaignDetailResponseDTO(
                    campaignId = campaign.campaignId,
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
            }.distinctBy { it.campaignId }
        return groupBy.first()
    }

    /**
     * 캠페인 정보 등록
     */
    @Transactional
    @Throws(CampaignException::class)
    fun setCampaign(request: SaveCampaignRequestDTO ): Boolean {

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
                            campaign = campaign,
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
                campaignTotalPrice = ( request.campaignPrice + request.reviewPoint ) * request.options.sumOf { it.recruitPeople },
                optionCount = request.options.size,
                reviewPoint = request.reviewPoint,
                totalCount = totalCount,
                activeDate = request.startSaleDateTime,
                finishDate = request.endSaleDateTime,
                activeCount = activeCount,
                sellerRequest = request.sellerRequest,
                dailyRecruitCount = totalCount / activeCount,
                startTime = request.startTime,
                endTime = request.endTime
            )
        )
        return true
    }

    /**
     * 캠페인 참여 신청
     * @param request: EnrollCampaignRequestDTO
     * @return Boolean
     */
    @Transactional
    @Throws(CampaignException::class, BasicException::class)
    fun enrollCampaign(request: EnrollCampaignRequestDTO): Boolean {

        val user = accountRepository.findById(request.userId)
            .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

        val campaign = campaignRepository.findById(request.campaignId)
            .orElseThrow { throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST) }

        val enroll = campaignEnrollRepository.findByCampaign(campaign)
        if (enroll.isNotEmpty()) { // 등록된 캠페인이 존재한다면?
            // 완료된 캠페인이 없는지 필터링
            val enrollNotCompletedCount = enroll
                .count { it?.enrollStatus != EnrollStatus.COMPLETE }
                .compareTo(0)

            // 완료되지 않은 캠페인이 존재하는 경우 에러를 뱉음.
            if (enrollNotCompletedCount != 0) {
                throw CampaignException(CampaignError.CAMPAIGN_NOT_COMPLETED)
            }
        }

        val option = campaignOptionsRepository.findById(request.campaignOptionId)
            .orElseThrow { throw CampaignException(CampaignError.OPTION_IS_NOT_EMPTY) }

        // 하위 옵션이 존재한다면
        val subOption = if (request.campaignSubOptionId != null) {

            if ( option.subOptions.none { it.id == request.campaignSubOptionId } ) {
                throw CampaignException(CampaignError.SUB_OPTION_IS_NOT_CONTAIN_OPTION)
            }

            campaignSubOptionsRepository.findByIdAndCampaign(request.campaignSubOptionId, campaign)
                ?: throw CampaignException(CampaignError.SUB_OPTION_IS_NOT_EMPTY)

        }else null

        // 캠페인 등록
        val campaignEnroll = campaignEnrollRepository.save(
            CampaignEnroll(
                user = user,
                campaign = campaign,
                options = option,
                subOptions = subOption,
                enrollDate = LocalDate.now(),
                enrollStatus = EnrollStatus.APPLY,
            )
        )

        // 예상 적립 포인트 등록
        val point = pointRepository.findByUser(user)
            ?: Point(
                    user = user,
                    remainPoint = 0,
                    totalChangePoint = 0,
                    expectPoint = 0,
                    createAt = LocalDateTime.now()
                )
        val detail = campaign.details.first()
        val totalPoint = if((subOption?.addPrice ?: 0) >= 0) {
            detail.campaignPrice + detail.reviewPoint + ( subOption?.addPrice ?: 0 )
        }else {
            detail.campaignPrice + detail.reviewPoint - ( subOption?.addPrice ?: 0 )
        }

        point.expectPoint = point.expectPoint + totalPoint

        this.pointRepository.save(point)

        // 포인트 상태 정보 등록
        this.pointAttributeRepository.save(
            PointAttribute(
                user = user,
                campaignEnroll = campaignEnroll,
                status = PointStatus.EXPECT,
                point = totalPoint,
                createAt = LocalDateTime.now()
            )
        )

        return true
    }

    @Transactional
    @Throws(CampaignException::class)
    fun updateCampaign(campaignId: Long, request: UpdateCampaignRequestDTO): Boolean {

        val campaign = campaignRepository.findById(campaignId)
            .orElseThrow {  throw CampaignException(CampaignError.OPTION_IS_NOT_EMPTY) }

        val campaignDetails = campaign.details.map { details ->
            CampaignDetails(
                id = details.id,
                campaignUrl = request.campaignLink,
                campaign = campaign,
                campaignImgUrl = request.campaignImgUrl,
                campaignPrice = request.campaignPrice,
                campaignTotalPrice = ( request.campaignPrice + request.reviewPoint ) * request.options.sumOf { it.recruitPeople } ,
                dailyRecruitCount = getLocalDateBetween(request.startSaleDateTime, request.endSaleDateTime).div(request.options.sumOf { it.recruitPeople }),
                startTime = request.startTime,
                endTime = request.endTime,
                totalCount = request.options.sumOf { it.recruitPeople },
                optionCount = request.options.size,
                reviewPoint = request.reviewPoint,
                activeDate = request.startSaleDateTime,
                finishDate = request.endSaleDateTime,
                activeCount = getLocalDateBetween(request.startSaleDateTime, request.endSaleDateTime),
                sellerRequest = request.sellerRequest,
                updateAt = LocalDateTime.now()
            )
        }.toMutableList()

        // 옵션 데이터 조회
        val campaignOptions = campaign.options

        // 요청 데이터 분류
        val updateOptions = request.options.associateBy { it.campaignOptionId }

        // 삭제 요청할 옵션 데이터 목록 취합
        val deleteOptions = campaignOptions.filter { it.id !in updateOptions.keys }.map { it }
        if ( deleteOptions.isNotEmpty() ) {
            this.campaignOptionsRepository.deleteAll(deleteOptions)
            this.campaignOptionsRepository.flush()
        }

        // 수정할 데이터 목록
        val updateOptionsList = campaignOptions
            .filter { it.id in updateOptions.keys }
            .mapIndexed { index, options ->
                CampaignOptions(
                    id = options.id,
                    title = request.options[index].optionTitle,
                    campaign = campaign,
                    optionType = request.optionType,
                    recruitPeople = if ( request.optionType == OptionType.SINGLE ) {
                        request.options.sumOf { it.recruitPeople }
                    }else {
                        request.options[index].subOption!!.sumOf { it.recruitPeople }
                    },
                    order = index,
                    updateAt = LocalDateTime.now()
                )
            }.toMutableList()

        if ( request.optionType == OptionType.MULTI ) {
            // 수정 요청 하위 옵션 데이터
            val updateSubOptionsMap = mutableMapOf<Long, UpdateCampaignRequestDTO.SubOptions>()
            request.options.map {
                it.subOption!!.forEach { option ->
                    updateSubOptionsMap[option.campaignSubOptionId] = option
                }
            }

            val deleteSubOptions = campaign.subOptions.filter { it.id !in updateSubOptionsMap.keys }.map { it }
            if (deleteSubOptions.isNotEmpty()) {
                this.campaignSubOptionsRepository.deleteAll(deleteSubOptions)
                this.campaignSubOptionsRepository.flush()
            }

            // 수정 데이터 취합
            val updateSubOptionList = mutableListOf<CampaignSubOptions>()

            request.options.mapIndexed { index, option ->
                option.subOption?.mapIndexed { subOptionIdx, subOptions ->
                    updateSubOptionList.add(
                        CampaignSubOptions(
                            id = subOptions.campaignSubOptionId,
                            title = subOptions.subOptionTitle,
                            campaign = campaign,
                            campaignOptions = updateOptionsList[index],
                            recruitPeople = subOptions.recruitPeople,
                            addPrice = subOptions.addPrice,
                            order = subOptionIdx,
                            updateAt = LocalDateTime.now()
                        )
                    )
                }
            }
            campaign.subOptions = updateSubOptionList
        }
        campaign.options = updateOptionsList
        campaign.details = campaignDetails
        campaign.companyName = request.companyName
        campaign.campaignTitle = request.productTitle
        campaign.campaignCategory = request.category
        campaign.campaignTitle = request.productTitle
        campaign.updateAt = LocalDateTime.now()

        this.campaignRepository.save(campaign) // 캠페인 데이터 수정

        return true
    }

    @Transactional
    @Throws(CampaignException::class, BasicException::class, PointException::class)
    fun getMyCampaigns(userId: Long): MyCampaignResponseDTO {
        // 사용자 정보 조회
        val user = accountRepository.findById(userId)
            .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

        // 사용자 캠페인 목록 정보 조회
        val enrollCampaigns = campaignEnrollRepository.findByUser(user)

        val myCampaigns = enrollCampaigns
            ?.map {
            MyCampaignResponseDTO.MyCampaigns(
                campaignId = it.id,
                campaignStatus = it.enrollStatus,
                registerDate = it.createAt,
                campaignImgUrl = it.campaign?.details?.first()?.campaignImgUrl,
                campaignTitle = it.campaign?.campaignTitle,
                campaignLink = it.campaign?.details?.first()?.campaignUrl
            )
        }

        val point = pointRepository.findByUser(user)
            ?: pointRepository.save(Point(
                user = user,
                remainPoint = 0,
                expectPoint = 0,
                totalChangePoint = 0,
                createAt = LocalDateTime.now()
            ))


        val qPointAttribute = QPointAttribute.pointAttribute

        val query = queryFactory
            .select(
                Expressions.numberTemplate(
                    Long::class.java,
                    "SUM({0})",
                    qPointAttribute.point
                )
            )
            .from(qPointAttribute)

        val expectPoint = ( query.where(
            qPointAttribute.user.eq(user)
                .and(qPointAttribute.status.eq(PointStatus.EXPECT))
        ).fetchOne() ?: 0 ).toInt()

        val changeTotalPoint = ( query.where(
            qPointAttribute.user.eq(user)
                .and(qPointAttribute.status.eq(PointStatus.COMPLETE))
        ).fetchOne() ?: 0 ).toInt()

        val myCampaignUserInfo = MyCampaignResponseDTO.MyCampaignUserInfo(
            name = user.name,
            loginId = user.username,
            expectPoint = expectPoint,
            totalPoint = changeTotalPoint,
            remainPoint = point.remainPoint
        )

        return MyCampaignResponseDTO(
            joinCount = enrollCampaigns?.count{ it.enrollStatus == EnrollStatus.APPLY },
            progressCount = enrollCampaigns?.count{ it.enrollStatus == EnrollStatus.PROGRESS },
            inspectCount = enrollCampaigns?.count{ it.enrollStatus == EnrollStatus.INSPECT },
            reviewCount = enrollCampaigns?.count{ it.enrollStatus == EnrollStatus.REVIEW },
            userInfo = myCampaignUserInfo,
            myCampaigns = myCampaigns
        )
    }

    /**
     * 후기 작성 서비스
     * @param image: MultipartFile
     * @param request: EnrollReviewRequestDTO
     * @return Boolean
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun setReviewImage(image: MultipartFile, request: EnrollReviewRequestDTO): Boolean {

        val enroll = campaignEnrollRepository.findById(request.campaignEnrollId)
            .orElseThrow { throw CampaignException(CampaignError.REGISTER_CAMPAIGN_NOT_EXIST) }

        if ( enroll.user?.id != request.userId ) {
            throw CampaignException(CampaignError.REGISTER_CAMPAIGN_NOT_EXIST)
        }

        // 해당 등록 캠페인 상태 정보
        when ( enroll.enrollStatus ) {
            // 상태값이 후기작성 상태일 경우
            EnrollStatus.REVIEW -> {
                // 캠페인 정보 조회 (일련번호 디렉토리 값으로 만들기 위해 조회)
                val campaign = enroll.campaign
                    ?: throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST)
                // AmazonS3 이미지 파일 업로드
                val fileImgUrl = amazonS3Service.uploadMultipartFile(image, "${campaign.id}")
                // 이미지 URL 업로드
                enroll.reviewImgUrl = fileImgUrl
                enroll.enrollStatus = EnrollStatus.INSPECT
                enroll.updateAt = LocalDateTime.now()
                this.campaignEnrollRepository.save(enroll)
                return true
            }
            // 예외의 경우
            else -> {
                throw CampaignException(CampaignError.CAMPAIGN_REVIEW)
            }
        }
    }

    /**
     * 주문번호 작성 서비스
     * @param request: EnrollProductOrderNoRequestDTO
     * @return Boolean
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun setProductOrderNo(request: EnrollProductOrderNoRequestDTO): Boolean {

        val enroll = campaignEnrollRepository.findById(request.campaignEnrollId)
            .orElseThrow { throw CampaignException(CampaignError.REGISTER_CAMPAIGN_NOT_EXIST) }

        if ( enroll.user?.id != request.userId ) {
            throw CampaignException(CampaignError.REGISTER_CAMPAIGN_NOT_EXIST)
        }

        return if ( enroll.enrollStatus == EnrollStatus.PROGRESS ) {
            enroll.orderNo = request.orderNo
            enroll.enrollStatus = EnrollStatus.REVIEW
            enroll.updateAt = LocalDateTime.now()
            this.campaignEnrollRepository.save(enroll)
            return true
        }else {
            throw CampaignException(CampaignError.CAMPAIGN_ORDER_NO)
        }
    }

    /**
     * 시작일과 종료일의 차이 값
     * @param startDate: LocalDate,
     * @param endDate: LocalDate
     * @return Long
     * @exception CampaignException
     */
    @Throws(CampaignException::class)
    private fun getLocalDateBetween(startDate: LocalDate, endDate: LocalDate): Long {
        if ( startDate.isBefore(endDate) ) {
            return ChronoUnit.DAYS.between(startDate,endDate)
        }else {
            throw CampaignException(CampaignError.START_DATE_SET)
        }
    }
}
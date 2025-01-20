package tech.server.reviral.api.campaign.service

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import tech.server.reviral.api.account.repository.AccountRepository
import tech.server.reviral.api.campaign.event.CampaignDetailsEventListener
import tech.server.reviral.api.campaign.model.dto.*
import tech.server.reviral.api.campaign.model.entity.*
import tech.server.reviral.api.campaign.model.enums.*
import tech.server.reviral.api.campaign.repository.*
import tech.server.reviral.api.point.model.entity.Point
import tech.server.reviral.api.point.model.entity.PointAttribute
import tech.server.reviral.api.point.model.enums.ExchangeStatus
import tech.server.reviral.api.point.model.enums.PointStatus
import tech.server.reviral.api.point.repository.PointAttributeRepository
import tech.server.reviral.api.point.repository.PointExchangeRepository
import tech.server.reviral.api.point.repository.PointRepository
import tech.server.reviral.common.config.aws.AmazonS3Service
import tech.server.reviral.common.config.response.exception.BasicException
import tech.server.reviral.common.config.response.exception.CampaignException
import tech.server.reviral.common.config.response.exception.PointException
import tech.server.reviral.common.config.response.exception.enums.BasicError
import tech.server.reviral.common.config.response.exception.enums.CampaignError
import tech.server.reviral.common.config.response.exception.enums.PointError
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
    private val pointExchangeRepository: PointExchangeRepository,
    private val queryFactory: JPAQueryFactory,
    private val amazonS3Service: AmazonS3Service,
    private val applicationEventPublisher: ApplicationEventPublisher
){

    /**
     * 캠페인 정보 다중 조회
     * @param category: String?
     * @param platform: String?
     * @param status: String?
     * @param pageable: Pageable
     * @return List<CampaignCardResponseDTO>
     */
    @Transactional
    @Throws(CampaignException::class)
    fun searchCampaigns(
        status: String?,
        pageable: Pageable
    ): List<CampaignCardResponseDTO> {

        val qCampaign = QCampaign.campaign
        val qCampaignDetails = QCampaignDetails.campaignDetails

        val now = LocalDate.now()

        val query = queryFactory
            .select(
                Projections.fields(
                    CampaignCardResponseDTO::class.java,
                    qCampaignDetails.id.`as`("campaignDetailsId"),
                    qCampaign.campaignTitle.`as`("campaignTitle"),
                    qCampaign.campaignStatus.`as`("campaignStatus"),
                    qCampaign.campaignPlatform.`as`("campaignPlatform"),
                    qCampaign.campaignImgUrl.`as`("campaignImgUrl"),
                    qCampaign.reviewPoint.`as`("campaignPoint"),
                    qCampaignDetails.sellerStatus.`as`("sellerStatus"),
                    qCampaignDetails.recruitCount.`as`("totalCount"),
                    qCampaign.campaignPrice.`as`("campaignPrice"),
                    qCampaignDetails.joinCount.`as`("joinCount"),
                    qCampaignDetails.applyDate.`as`("applyDate"),
                )
            )
            .from(qCampaign)
            .join(qCampaignDetails).on(
                qCampaignDetails.campaign.id.eq(qCampaign.id)
            )

        val booleanBuilder = BooleanBuilder()

        // 추가 조건
        if (status != null) { // 카테고리
            when(status) {
                "wait" -> {
                    booleanBuilder.and(
                        qCampaignDetails.sellerStatus.eq(SellerStatus.WAIT)
                            .and(qCampaignDetails.applyDate.eq(now.plusDays(1)))
                    )

                    return query
                        .where(booleanBuilder)
                        .fetch()
                }
                "progress" -> {
                    booleanBuilder.and(
                        qCampaignDetails.sellerStatus.eq(SellerStatus.ACTIVE)
                            .and(qCampaignDetails.applyDate.eq(now))
                    )

                    return query
                        .where(booleanBuilder)
                        .distinct()
                        .fetch()
                }
                "best" -> {
                    booleanBuilder.and(
                        qCampaignDetails.applyDate.eq(now)
                    )

                    return query
                        .where(booleanBuilder)
                        .limit(5)
                        .orderBy(qCampaignDetails.joinCount.desc())
                        .distinct()
                        .fetch()
                }
            }
        }else { // 이런 캠페인을 찾으셨나요
            booleanBuilder.and(
                qCampaignDetails.applyDate.eq(now)
                    .and(qCampaignDetails.sellerStatus.eq(SellerStatus.ACTIVE))
            ).or(
                qCampaignDetails.applyDate.eq(now.plusDays(1))
                    .and(qCampaignDetails.sellerStatus.eq(SellerStatus.WAIT))
            )
        }

        // 순차정렬 enum 커스텀
        val order = CaseBuilder()
            .`when`(qCampaign.campaignStatus.eq(CampaignStatus.PROGRESS)).then(0)
            .`when`(qCampaign.campaignStatus.eq(CampaignStatus.WAIT)).then(1)
            .`when`(qCampaign.campaignStatus.eq(CampaignStatus.FINISH)).then(2)
            .otherwise(3)

        return query
            .distinct()
            .where(booleanBuilder)
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(order.asc())
            .fetch()
    }

    /**
     * 캠페인 상세 정보 조회
     * @param campaignDetailsId: Long
     * @return CampaignDetailResponseDTO
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun getCampaign(campaignDetailsId: Long): CampaignDetailResponseDTO {

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
                    qCampaign.campaignPlatform.`as`("campaignPlatform"),
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
                qCampaignDetails.id.eq(campaignDetailsId)
            )
            .groupBy(
                qCampaignDetails.id,
                qCampaign.id,
                qCampaignOptions.id,
                qCampaignSubOptions.id
            )
            .distinct()
            .fetch()

        val groupBy = query.groupBy { it.campaignDetailsId }
            .map { (_, campaigns) ->
                val campaign = campaigns.first()

                CampaignDetailResponseDTO(
                    campaignDetailsId = campaign.campaignDetailsId,
                    campaignTitle = campaign.campaignTitle,
                    campaignPlatform = campaign.campaignPlatform,
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
            }.distinctBy { it.campaignDetailsId }
        return groupBy.first()
    }

    /**
     * 캠페인 정보 등록
     * @param request: SaveCampaignRequestDTO
     * @return Boolean
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun setCampaign(request: SaveCampaignRequestDTO ): Boolean {

        // 총 모집인원
        val totalRecruitCount = if ( request.optionType == OptionType.SINGLE ) {
            request.options.sumOf { it.recruitPeople }
        }else {
            request.options.map {
                it.subOption?.sumOf { it.recruitPeople }
            }.sumOf { it ?: 0 }
        }

        // 활성화 기간
        val activeCount = getLocalDateBetween(request.startSaleDateTime, request.endSaleDateTime)

        // 캠페인 정보 등록
        val campaign = campaignRepository.save(Campaign(
            companyName = request.companyName,
            campaignPlatform = request.platform,
            campaignTitle = request.productTitle,
            campaignStatus = CampaignStatus.WAIT,
            campaignCategory = request.category,
            campaignUrl = request.campaignLink,
            campaignImgUrl = request.campaignImgUrl,
            campaignPrice = request.campaignPrice,
            campaignProgressPrice = request.progressPrice,
            campaignTotalPrice = ( request.campaignPrice + request.reviewPoint ) * request.options.sumOf { it.recruitPeople },
            optionCount = request.options.size,
            reviewPoint = request.reviewPoint,
            totalRecruitCount = totalRecruitCount,
            activeDate = request.startSaleDateTime,
            finishDate = request.endSaleDateTime,
            sellerRequest = request.sellerRequest,
            sellerGuide = request.sellerGuide,
            dailyRecruitCount = totalRecruitCount / activeCount,
            startTime = request.startTime,
            endTime = request.endTime
        ))

        val now = LocalDate.now()

        // 캠페인 진행날짜별 데이터 등록
        for ( i in 0 .. activeCount ) {
            campaignDetailsRepository.save(
                CampaignDetails(
                    campaign = campaign,
                    applyDate = now.plusDays(i),
                    sellerStatus = SellerStatus.WAIT,
                    sortNo = i.toInt(),
                    recruitCount = totalRecruitCount / activeCount
                )
            )
        }

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
                }
            }
        }
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

        // 사용자 정보 조회
        val user = accountRepository.findById(request.userId)
            .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

        // 캠페인 상세정보 조회
        val campaignDetails = campaignDetailsRepository.findById(request.campaignDetailsId)
            .orElseThrow { throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST) }

        // 캠페인 정보 조회
        val campaign = campaignDetails.campaign
            ?: throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST)

        // 동일한 캠페인을 진행한 이력정보 조회
        val enrollList = campaignEnrollRepository.findByCampaignAndUserOrderByCreateAtDesc(campaign,user)

        // 취소한 경우에 동일한 날짜에 신청한다면 오류발생
        if ( enrollList.count { it?.enrollStatus == EnrollStatus.CANCEL && it.enrollDate == LocalDate.now() }  > 0 ) {
            throw CampaignException(CampaignError.CAMPAIGN_CANCEL_JOIN)
        }

        val enroll = enrollList.filter { it?.enrollStatus != EnrollStatus.CANCEL  }

        // 중복 설정이 참 이라면
        if ( campaign.isDuplicated ) {
            throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_DUPLICATED)
        }

        // 캠페인 참여 인원이 꽉 찼을 경우,
        if ( campaignDetails.joinCount >= campaignDetails.recruitCount ) {
            applicationEventPublisher.publishEvent(
                CampaignDetailsEventListener.CampaignDetailsUpdateEvent(campaignDetails)
            )
            throw CampaignException(CampaignError.CAMPAIGN_FULL_RECRUIT)
        }

        // 사용자의 플랫폼의 아이디가 존재하지 않는다면 오류
        when (campaign.campaignPlatform) {
            CampaignPlatform.COUPANG -> {
                if ( user.cpId == null ) {
                    throw CampaignException(CampaignError.CAMPAIGN_CP_ID_NULL)
                }
            }
            CampaignPlatform.NAVER -> {
                if (user.nvId == null) throw CampaignException(CampaignError.CAMPAIGN_NV_ID_NULL)
            }
            else -> {}
        }

        // 동일한 캠페인을 진행한 이력이 존재한다면 유효성 검사 진행
        if ( enroll.isNotEmpty() ) {
            // 캠페인의 상태값이 완료값이 아니라면 오류 발생
            if ( enroll.count{ it?.enrollStatus != EnrollStatus.COMPLETE } != 0 ) {
                throw CampaignException(CampaignError.CAMPAIGN_NOT_COMPLETED)
            }
            // 동일한 캠페인을 진행할 때, 중복 가능일자가 내에 존재한다면 오류 발생
            val today = LocalDate.now()
            val enrollDate = enroll.first()?.enrollDate
            val endDate = enrollDate?.plusDays(campaign.duplicatedDate ?: 14 )
            if ( !today.isBefore(enrollDate) && !today.isAfter(endDate) ) {
                throw CampaignException(CampaignError.CAMPAIGN_START_NOT_YET)
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
                campaignDetails = campaignDetails,
                cancelYn = false
            )
        )

        // 캠페인 참여 인원 등록
        campaignDetails.joinCount = campaignDetails.joinCount + 1
        campaignDetails.updateAt = LocalDateTime.now()
        this.campaignDetailsRepository.save(campaignDetails)

        // 캠페인 참여 인원이 모두 찼을 경우,
        if ( campaignDetails.joinCount == campaignDetails.recruitCount ) {
            applicationEventPublisher.publishEvent(
                CampaignDetailsEventListener.CampaignDetailsUpdateEvent(campaignDetails)
            )
        }

        // 예상 적립 포인트 등록
        val point = pointRepository.findByUser(user)
            ?: Point(
                    user = user,
                    remainPoint = 0,
                    totalChangePoint = 0,
                    expectPoint = 0,
                    createAt = LocalDateTime.now()
                )

        val totalPoint = if((subOption?.addPrice ?: 0) >= 0) {
            campaign.campaignPrice + campaign.reviewPoint + ( subOption?.addPrice ?: 0 )
        }else {
            campaign.campaignPrice + campaign.reviewPoint - ( subOption?.addPrice ?: 0 )
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

        val activeDate = getLocalDateBetween(request.startSaleDateTime, request.endSaleDateTime)

        campaign.campaignTitle = request.productTitle
        campaign.campaignImgUrl = request.campaignImgUrl
        campaign.campaignPrice = request.campaignPrice
        campaign.campaignTotalPrice = ( request.campaignPrice + request.reviewPoint ) * request.options.sumOf { it.recruitPeople }
        campaign.dailyRecruitCount = activeDate.div(request.options.sumOf { it.recruitPeople })
        campaign.startTime = request.startTime
        campaign.endTime = request.endTime
        campaign.totalRecruitCount = request.options.sumOf { it.recruitPeople }
        campaign.optionCount = request.options.size
        campaign.reviewPoint = request.reviewPoint
        campaign.activeDate = request.startSaleDateTime
        campaign.finishDate = request.endSaleDateTime
        campaign.sellerRequest = request.sellerRequest
        campaign.companyName = request.companyName
        campaign.campaignTitle = request.productTitle
        campaign.campaignCategory = request.category
        campaign.updateAt = LocalDateTime.now()

        campaign.updateAt = LocalDateTime.now()

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

        this.campaignRepository.save(campaign) // 캠페인 데이터 수정

        return true
    }

    /**
     * 마이캠페인 목록 조회 서비스
     * @param userId: Long
     * @return MyCampaignResponseDTO
     * @exception CampaignException
     * @exception BasicException
     * @exception PointException
     */
    @Transactional
    @Throws(CampaignException::class, BasicException::class, PointException::class)
    fun getMyCampaigns(userId: Long): MyCampaignResponseDTO {
        // 사용자 정보 조회
        val user = accountRepository.findById(userId)
            .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

        // 사용자 캠페인 목록 정보 조회
        val enrollCampaigns = campaignEnrollRepository.findByUserAndCancelYn(user,false)

        val myCampaigns = enrollCampaigns
            ?.map {
            MyCampaignResponseDTO.MyCampaigns(
                campaignEnrollId = it.id,
                campaignStatus = it.enrollStatus,
                registerDate = it.createAt,
                campaignImgUrl = it.campaign?.campaignImgUrl,
                campaignTitle = it.campaign?.campaignTitle,
                campaignLink = it.campaign?.campaignUrl,
                sellerGuide = it.campaign?.sellerGuide,
                orderStatus = it.inspectOrderYn,
                reviewStatus = it.inspectReviewYn
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

        val exchange = pointExchangeRepository.findByUserAndStatus(user, ExchangeStatus.REQ)

        val myCampaignUserInfo = MyCampaignResponseDTO.MyCampaignUserInfo(
            name = user.name,
            loginId = user.username,
            expectPoint = point.expectPoint,
            totalPoint = point.totalChangePoint,
            remainPoint = point.remainPoint,
            exchangePoint = exchange.filter { it?.status == ExchangeStatus.REQ }.sumOf{ it?.pointValue ?: 0 }
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
     * @param request: EnrollReviewRequestDTO
     * @return Boolean
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun setReviewImage(request: EnrollReviewRequestDTO, image: MultipartFile): Boolean {

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
                val fileImgUrl = amazonS3Service.uploadMultipartFile(image, "${campaign.id}/review")

                enroll.enrollStatus = EnrollStatus.INSPECT
                enroll.updateAt = LocalDateTime.now()
                this.campaignEnrollRepository.save(enroll)
                return true
            }
            EnrollStatus.INSPECT -> {
                // 캠페인 정보 조회 (일련번호 디렉토리 값으로 만들기 위해 조회)
                val campaign = enroll.campaign
                    ?: throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST)

                // AmazonS3 이미지 파일 업로드
                val fileImgUrl = amazonS3Service.uploadMultipartFile(image, "${campaign.id}/review")

                // 이미지 URL 업로드
                enroll.reviewImgUrl = fileImgUrl
                enroll.inspectReviewYn  = null // 검토중으로 상태값 재변경
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
    fun setProductOrderImageUrl(request: EnrollReviewRequestDTO, image: MultipartFile): Boolean {

        val enroll = campaignEnrollRepository.findById(request.campaignEnrollId)
            .orElseThrow { throw CampaignException(CampaignError.REGISTER_CAMPAIGN_NOT_EXIST) }

        if ( enroll.user?.id != request.userId ) {
            throw CampaignException(CampaignError.REGISTER_CAMPAIGN_NOT_EXIST)
        }

        when ( enroll.enrollStatus ) {
            EnrollStatus.PROGRESS -> {
                // 캠페인 정보 조회 (일련번호 디렉토리 값으로 만들기 위해 조회)
                val campaign = enroll.campaign
                    ?: throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST)
                // AmazonS3 이미지 파일 업로드
                val fileImgUrl = amazonS3Service.uploadMultipartFile(image, "${campaign.id}/order")
                // 해당 상태가 이미지 재검수 요청이라면
                enroll.orderImageUrl = fileImgUrl
                enroll.enrollStatus = EnrollStatus.REVIEW
                enroll.updateAt = LocalDateTime.now()
                this.campaignEnrollRepository.save(enroll)
                return true
            }
            EnrollStatus.INSPECT -> {
                // 캠페인 정보 조회 (일련번호 디렉토리 값으로 만들기 위해 조회)
                val campaign = enroll.campaign
                    ?: throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST)

                // AmazonS3 이미지 파일 업로드
                val fileImgUrl = amazonS3Service.uploadMultipartFile(image, "${campaign.id}/review")

                // 해당 상태가 이미지 재검수 요청이라면
                // 이미지 URL 업로드
                enroll.reviewImgUrl = fileImgUrl
                enroll.inspectOrderYn  = null // 검토중으로 상태값 재변경
                enroll.updateAt = LocalDateTime.now()
                this.campaignEnrollRepository.save(enroll)
                return true
            }
            else -> {
                throw CampaignException(CampaignError.CAMPAIGN_ORDER_NO)
            }
        }
    }

    /**
     * 캠페인 등록 정보 삭제
     * @param request: DeleteCampaignEnrollRequestDTO
     * @return Boolean 삭제여부
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun deleteCampaignEnroll( request: DeleteCampaignEnrollRequestDTO ): Boolean {
        val enroll = campaignEnrollRepository.findById(request.campaignEnrollId)
            .orElseThrow { throw CampaignException(CampaignError.REGISTER_CAMPAIGN_NOT_EXIST) }

        if ( request.isForcedRevision || enroll.enrollStatus == EnrollStatus.APPLY && enroll.user?.id == request.userId) {
            enroll.cancelYn = true
            enroll.updateAt = LocalDateTime.now()
            enroll.enrollStatus = EnrollStatus.CANCEL
            this.campaignEnrollRepository.save(enroll)

            val pointAttribute = pointAttributeRepository.findByCampaignEnroll(enroll)
                ?: throw PointException(PointError.POINT_IS_NOT_EXIST)

            pointAttribute.status = PointStatus.CANCEL
            pointAttribute.updateAt = LocalDateTime.now()
            this.pointAttributeRepository.save(pointAttribute)

            val point = pointRepository.findByUser(enroll.user!!)
                ?: throw PointException(PointError.POINT_IS_NOT_EXIST)

            val minusPoint = point.expectPoint - pointAttribute.point
            point.expectPoint = if ( minusPoint <= 0 ) {
                0
            }else {
                minusPoint
            }
            point.updateAt = LocalDateTime.now()
            this.pointRepository.save(point)

        }else {
            throw CampaignException(CampaignError.CAMPAIGN_DO_NOT_CANCEL)
        }
        return true
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
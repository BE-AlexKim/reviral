package tech.server.reviral.api.bussiness.service

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.group.GroupBy
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import tech.server.reviral.api.account.model.entity.QUser
import tech.server.reviral.api.account.model.entity.QUserInfo
import tech.server.reviral.api.account.repository.AccountRepository
import tech.server.reviral.api.bussiness.model.dto.*
import tech.server.reviral.api.bussiness.model.enums.ImageCheck
import tech.server.reviral.api.campaign.model.dto.*
import tech.server.reviral.api.campaign.model.entity.*
import tech.server.reviral.api.campaign.model.enums.*
import tech.server.reviral.api.campaign.repository.*
import tech.server.reviral.api.point.model.dto.PointExchangeResponseDTO
import tech.server.reviral.api.point.model.enums.ExchangeStatus
import tech.server.reviral.api.point.model.enums.PointStatus
import tech.server.reviral.api.point.repository.PointExchangeRepository
import tech.server.reviral.api.point.repository.PointRepository
import tech.server.reviral.config.aws.AmazonS3Service
import tech.server.reviral.config.message.MessageService
import tech.server.reviral.common.AESEncryption
import tech.server.reviral.config.response.exception.BasicException
import tech.server.reviral.config.response.exception.CampaignException
import tech.server.reviral.config.response.exception.PointException
import tech.server.reviral.config.response.exception.enums.BasicError
import tech.server.reviral.config.response.exception.enums.CampaignError
import tech.server.reviral.config.response.exception.enums.PointError
import tech.server.reviral.common.DateUtil
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 *packageName    : tech.server.reviral.api.bussiness.service
 * fileName       : CampaignService
 * author         : joy58
 * date           : 2025-01-31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-31        joy58       최초 생성
 */
@Service
class BusinessService constructor(
    private val queryFactory: JPAQueryFactory,
    private val campaignRepository: CampaignRepository,
    private val campaignDetailsRepository: CampaignDetailsRepository,
    private val campaignOptionsRepository: CampaignOptionsRepository,
    private val campaignSubOptionsRepository: CampaignSubOptionsRepository,
    private val campaignEnrollRepository: CampaignEnrollRepository,
    private val pointRepository: PointRepository,
    private val accountRepository: AccountRepository,
    private val amazonS3Service: tech.server.reviral.config.aws.AmazonS3Service,
    private val pointExchangeRepository: PointExchangeRepository,
    private val messageService: MessageService,
    private val campaignGuideRepository: CampaignGuideRepository
){

    @Value("\${encrypt.aes.secret-key}")
    private lateinit var secretKey: String


    /**
     *  관리자 캠페인 목록 조회 서비스
     *  @param status: 캠페인 상태값,
     *  @param platform: 플랫폼 값,
     *  @param campaignTitle: 캠페인 제목,
     *  @param size: 조회할 목록 갯수,
     *  @param offset: 조회할 목록 페이지
     *  @return AdminCampaignsResponseDTO
     *  @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun getCampaigns(status: String?, platform: String?, campaignTitle: String?, size: Long, offset: Long): AdminCampaignsResponseDTO {

        val qCampaign = QCampaign.campaign
        val qCampaignDetails = QCampaignDetails.campaignDetails

        val booleanBuilder = BooleanBuilder()
        val orderSpecifiers = mutableListOf<OrderSpecifier<*>>()

        // campaignTitle 조건 추가
        if (campaignTitle != null) {
            booleanBuilder.and(qCampaign.campaignTitle.like("%$campaignTitle%"))
        }

        // status 조건 추가 및 정렬
        if (status != null) {
            when (CampaignStatus.valueOf(status.uppercase())) {
                CampaignStatus.WAIT -> {
                    booleanBuilder.and(qCampaign.campaignStatus.eq(CampaignStatus.WAIT))
                    orderSpecifiers.add(OrderSpecifier(Order.ASC, qCampaign.campaignStatus))
                }
                CampaignStatus.PROGRESS -> {
                    booleanBuilder.and(qCampaign.campaignStatus.eq(CampaignStatus.PROGRESS))
                    orderSpecifiers.add(OrderSpecifier(Order.ASC, qCampaign.campaignStatus))
                }
                CampaignStatus.FINISH -> {
                    booleanBuilder.and(qCampaign.campaignStatus.eq(CampaignStatus.FINISH))
                    orderSpecifiers.add(OrderSpecifier(Order.ASC, qCampaign.campaignStatus))
                }
            }
        }

        // platform 조건 추가 및 정렬
        if (platform != null) {
            when (CampaignPlatform.valueOf(platform.uppercase())) {
                CampaignPlatform.NAVER -> {
                    booleanBuilder.and(qCampaign.campaignPlatform.eq(CampaignPlatform.NAVER))
                    orderSpecifiers.add(OrderSpecifier(Order.ASC, qCampaign.campaignPlatform))
                }
                CampaignPlatform.COUPANG -> {
                    booleanBuilder.and(qCampaign.campaignPlatform.eq(CampaignPlatform.COUPANG))
                    orderSpecifiers.add(OrderSpecifier(Order.ASC, qCampaign.campaignPlatform))
                }
                CampaignPlatform.ETC -> {
                    booleanBuilder.and(qCampaign.campaignPlatform.eq(CampaignPlatform.ETC))
                    orderSpecifiers.add(OrderSpecifier(Order.ASC, qCampaign.campaignPlatform))
                }
            }
        }

        val query = queryFactory
            .select(
                Projections.constructor(
                    AdminCampaignsResponseDTO.AdminCampaignsResult::class.java,
                    qCampaign.id.`as`("campaignId"),
                    qCampaign.campaignStatus.`as`("campaignStatus"),
                    qCampaign.activeDate.`as`("startDate"),
                    qCampaign.finishDate.`as`("finishDate"),
                    qCampaign.campaignPlatform.`as`("campaignPlatform"),
                    qCampaign.campaignTitle.`as`("campaignTitle"),
                    Expressions.numberTemplate(
                        Long::class.java,
                        "({0} * {1}) + ({2} * {1})",
                        qCampaign.campaignPrice,
                        qCampaignDetails.recruitCount.sum(),
                        qCampaign.campaignProgressPrice
                    ).`as`("campaignTotalPrice"),
                    qCampaignDetails.recruitCount.sum().`as`("totalRecruitCount"),
                    qCampaignDetails.joinCount.sum().`as`("totalJoinCount"),
                    qCampaign.createAt.`as`("createAt")
                )
            )
            .from(qCampaign)
            .join(qCampaignDetails).on(qCampaign.eq(qCampaignDetails.campaign))
            .where(
                booleanBuilder
                .and(!qCampaignDetails.isDelete)
            ) // 조건 적용
            .groupBy(qCampaign.id)
            .orderBy(*orderSpecifiers.toTypedArray()) // 정렬 적용
            .offset(offset)
            .limit(size)

        val queryFetch = query.fetch()

        return AdminCampaignsResponseDTO(
            total = queryFetch.count(),
            campaigns = queryFetch
        )
    }

    /**
     * 캠페인 정보 등록
     * @param request: SaveCampaignRequestDTO
     * @return Boolean
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun setCampaign(images: List<MultipartFile>, request: SaveCampaignRequestDTO): Boolean {

        // 총 모집인원 계산
        val dailyRecruitCount = request.options.sumOf {
            when (request.optionType) {
                OptionType.SINGLE -> it.recruitPeople
                else -> it.subOption?.sumOf { subOption -> subOption.recruitPeople } ?: 0
            }
        }

        // 활성화 기간 계산
        val activeCount = maxOf(getLocalDateBetween(request.startSaleDateTime, request.endSaleDateTime),1L)

        // 일일 모집 인원 및 남은 모집 인원 계산
        val totalRecruitCount = ( ( dailyRecruitCount ) * activeCount ).toInt()

        // 캠페인 정보 등록
        val campaign = Campaign(
            companyName = request.companyName,
            campaignPlatform = request.platform,
            campaignTitle = request.productTitle,
            campaignStatus = CampaignStatus.WAIT,
            campaignCategory = request.category,
            campaignUrl = request.campaignLink,
            campaignImgUrl = request.campaignImgUrl,
            campaignPrice = request.campaignPrice,
            campaignProgressPrice = request.progressPrice,
            campaignTotalPrice = (request.campaignPrice + request.progressPrice) * totalRecruitCount,
            optionCount = request.options.size,
            reviewPoint = request.reviewPoint,
            totalRecruitCount = totalRecruitCount,
            activeDate = request.startSaleDateTime,
            finishDate = request.endSaleDateTime,
            sellerRequest = request.sellerRequest,
            dailyRecruitCount = dailyRecruitCount.toLong(),
            startTime = request.startTime,
            endTime = request.endTime,
            cpType = request.cpType,
            isNotWorkDay = request.isNotWorkDay
        )

        val strengthText = listOfNotNull(
            request.firstStrengthText,
            request.secondStrengthText,
            request.thirdStrengthText,
            request.forthStrengthText
        ).mapIndexed { i, v ->
            "$i.$v"
        }.joinToString("\n")

        campaign.guide =  this.campaignGuideRepository.save(CampaignGuide(
            campaign = campaign,
            guideType = GuideType.PAYMENT,
            textLength = request.textLength,
            strengthText = strengthText,
            condition = request.condition,
            etcText = request.etcText
        ))

        // 진행일자 생성
        val activeDays = DateUtil().getWorkDays(request.startSaleDateTime,request.endSaleDateTime, request.isNotWorkDay)

        val campaignDetails = activeDays.mapIndexed { i,v ->
            CampaignDetails(
                campaign = campaign,
                applyDate = v,
                sellerStatus = SellerStatus.WAIT,
                sortNo = i,
                recruitCount = dailyRecruitCount.toLong()
            )
        }
        this.campaignDetailsRepository.saveAll(campaignDetails)

        // 캠페인 옵션 정보 등록
        val campaignOptions = request.options.mapIndexed { index, option ->
            CampaignOptions(
                campaign = campaign,
                title = option.optionTitle,
                optionType = request.optionType,
                order = index,
                recruitPeople = when (request.optionType) {
                    OptionType.SINGLE -> option.recruitPeople
                    else -> option.subOption?.sumOf { it.recruitPeople } ?: 0
                }
            )
        }
        val savedOptions = campaignOptionsRepository.saveAll(campaignOptions)

        // 조합형 옵션의 하위 옵션 등록
        val campaignSubOptions = request.options.flatMapIndexed { index, option ->
            val campaignOption = savedOptions[index]
            option.subOption?.mapIndexed { subIndex, subOption ->
                CampaignSubOptions(
                    campaign = campaign,
                    campaignOptions = campaignOption,
                    title = subOption.subOptionTitle,
                    order = subIndex,
                    addPrice = subOption.addPrice,
                    recruitPeople = subOption.recruitPeople
                )
            }.orEmpty()
        }
        if (campaignSubOptions.isNotEmpty()) {
            campaignSubOptionsRepository.saveAll(campaignSubOptions)
        }

        // 이미지 데이터가 존재하면서 타입이 포토라면
        if ( images.isNotEmpty() && request.category == CampaignCategory.PT ) {
            images.forEach {
                amazonS3Service.uploadMultipartFile(it,"${campaign.id}/review/image")
            }
        }

        return true
    }

    /**
     * 캠페인 정보 수정 모델
     * @param request: 캠페인 수정 모델
     * @return Boolean
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun updateCampaign(request: UpdateCampaignRequestDTO): Boolean {
        val campaign = campaignRepository.findById(request.campaignId)
            .orElseThrow { throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST) }

        val active = campaign.details.count {
            it.sellerStatus != SellerStatus.WAIT
         || it.sellerStatus != SellerStatus.ACTIVE
        }

        if ( active > 0 ) { // 캠페인이 진행중 또는 활성

        }

        request.companyName.let { campaign.companyName = it }
        request.platform.let { campaign.campaignPlatform = it }
        request.category.let { campaign.campaignCategory = it }
        request.productTitle.let { campaign.campaignTitle = it }
        request.campaignImgUrl.let { campaign.campaignImgUrl = it!! }

        return true
    }

    /**
     * 캠페인 날짜 추가하기
     * @param request: 캠페인 일련번호
     * @return 추가 여부
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun addToCampaignDetail(request: AddCampaignDetailRequestDTO): Boolean {
        val campaign = campaignRepository.findById(request.campaignId)
            .orElseThrow { throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST) }

        val lastCampaignDetail = campaign.details
            .last { !it.isDelete }

        campaign.finishDate = if ( campaign?.isNotWorkDay == true ) {
            DateUtil().adjustToNearestWeekDay(lastCampaignDetail.applyDate.plusDays(1))
        }else {
            lastCampaignDetail.applyDate.plusDays(1)
        }

        campaign.totalRecruitCount =  campaign.totalRecruitCount + lastCampaignDetail.recruitCount.toInt()

        campaignDetailsRepository.save(CampaignDetails(
            campaign = campaign,
            applyDate = campaign.finishDate,
            sellerStatus = SellerStatus.WAIT,
            joinCount = 0,
            isDelete = false,
            sortNo = lastCampaignDetail.sortNo + 1,
            recruitCount = lastCampaignDetail.recruitCount,
        ))

        return true
    }

    @Transactional
    @Throws(CampaignException::class)
    fun deleteCampaignDetail(campaignDetailsId: Long): Boolean {
        val campaignDetails = campaignDetailsRepository.findById(campaignDetailsId)
            .orElseThrow { throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST) }

        val campaign = campaignDetails.campaign

        when ( campaignDetails.sellerStatus ) {
            SellerStatus.WAIT -> {
                val lastCampaign = campaignDetails.campaign?.details
                    ?.last { !it.isDelete }

                if (  lastCampaign?.applyDate == campaignDetails.applyDate ) {

                    if ( campaignDetails.campaign.isNotWorkDay ) {
                        campaignDetails.campaign.finishDate = DateUtil().adjustToBackNearestWeekDay(campaignDetails.applyDate.minusDays(1))
                    }else {
                        campaignDetails.campaign.finishDate = campaignDetails.applyDate.minusDays(1)
                    }
                }
                val totalRecruitCount = campaign?.totalRecruitCount?.minus(campaignDetails.recruitCount)?.toInt()

                if (totalRecruitCount != null) {
                    campaignDetails.campaign.totalRecruitCount = if ( totalRecruitCount <= 0 ) 0 else totalRecruitCount
                }

                campaignDetails.isDelete = true
                campaignDetails.updateAt = LocalDateTime.now()
            }
            SellerStatus.ACTIVE -> {
                if ( campaignDetails.joinCount > 0 ) {
                    throw CampaignException(CampaignError.CAMPAIGN_EXIST_JOIN_PEOPLE)
                }else {
                    val lastCampaign = campaignDetails.campaign?.details
                        ?.last { !it.isDelete }
                    if (  lastCampaign?.applyDate == campaignDetails.applyDate ) {

                        if (campaignDetails.campaign.isNotWorkDay) {
                            campaignDetails.campaign.finishDate =
                                DateUtil().adjustToBackNearestWeekDay(campaignDetails.applyDate.minusDays(1))
                        } else {
                            campaignDetails.campaign.finishDate = campaignDetails.applyDate.minusDays(1)
                        }
                    }
                    val totalRecruitCount = (( campaignDetails.campaign?.totalRecruitCount ?: 0 ) - campaignDetails.recruitCount ).toInt()
                    campaignDetails.campaign?.totalRecruitCount = if ( totalRecruitCount < 0 ) 0 else totalRecruitCount
                    campaignDetails.isDelete = true
                    campaignDetails.updateAt = LocalDateTime.now()
                }
            }
            else -> {
                throw CampaignException(CampaignError.CAMPAIGN_DO_NOT_DELETE)
            }
        }
        return true
    }


    /**
     * 캠페인 상세목록 조회 서비스
     * @param campaignId: 캠페인 일련번호
     * @return AdminCampaignsDetailsResponseDTO
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun getCampaignDetails(campaignId: Long): MutableList<AdminCampaignDetailsResponseDTO> {

        val qCampaign = QCampaign.campaign
        val qCampaignDetails = QCampaignDetails.campaignDetails

        val query = queryFactory
            .select(
                Projections.fields(
                    AdminCampaignDetailsResponseDTO::class.java,
                    qCampaign.id.`as`("campaignId"),
                    qCampaignDetails.id.`as`("campaignDetailsId"),
                    Expressions.stringTemplate(
                        "case " +
                                "when {0} = 'ACTIVE' AND {1} < {2} then '모집 진행' " +
                                "when {0} = 'ACTIVE' AND {1} = {2} then '모집 완료' " +
                                "when {0} = 'WAIT' then '캠페인 대기' " +
                                "when {0} = 'PROGRESS' then '캠페인 진행' " +
                                "when {0} = 'COMPLETE' then '캠페인 완료' " +
                                "else '오류' end",
                        qCampaignDetails.sellerStatus,
                        qCampaignDetails.joinCount,
                        qCampaignDetails.recruitCount
                    ).`as`("sellerStatus"),
                    qCampaignDetails.applyDate.`as`("applyDate"),
                    qCampaign.campaignTitle.`as`("campaignTitle"),
                    qCampaignDetails.recruitCount.`as`("recruitCount"),
                    qCampaignDetails.joinCount.`as`("joinCount"),
                    qCampaignDetails.isDelete.`as`("isDelete"),
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
            .join(qCampaignDetails).on(qCampaignDetails.campaign.eq(qCampaign))
            .where(qCampaign.id.eq(campaignId)
                .and(qCampaignDetails.isDelete.eq(false))
            )

        val addQuery = query.fetch()
            .map { dto ->
                dto.campaignDetailsUrl = generatedUrl(dto.campaignId.toString())
                dto
            }.toMutableList()

        return addQuery
    }

    @Transactional
    @Throws(CampaignException::class)
    fun getCampaignDetail(request: CampaignDetailRequestDTO): AdminCampaignDetailResponseDTO? {

        val campaignId = decryptEncodeUrl(request.encodeUrl).toLong()

        val qCampaign = QCampaign.campaign
        val qCampaignDetails = QCampaignDetails.campaignDetails
        val qCampaignEnroll = QCampaignEnroll.campaignEnroll
        val qUser = QUser.user
        val qUserInfo = QUserInfo.userInfo

        return queryFactory
            .from(qCampaign)
            .join(qCampaignDetails).on(qCampaignDetails.campaign.eq(qCampaign))
            .leftJoin(qCampaignEnroll).on(qCampaignEnroll.campaignDetails.eq(qCampaignDetails)) // Left join allows null values
            .leftJoin(qUser).on(qCampaignEnroll.user.eq(qUser)) // Also make this a left join for flexibility
            .leftJoin(qUserInfo).on(qUser.userInfo.eq(qUserInfo))
            .where(
                qCampaign.id.eq(campaignId)
                    .and(qCampaignDetails.applyDate.eq(request.applyDate))
            )
            .transform(
                GroupBy.groupBy(qCampaignDetails.id).list(
                    Projections.fields(
                        AdminCampaignDetailResponseDTO::class.java,
                        qCampaign.id.`as`("campaignId"),
                        qCampaignDetails.id.`as`("campaignDetailsId"),
                        qCampaign.companyName.`as`("companyName"),
                        qCampaign.campaignTitle.`as`("campaignTitle"),
                        qCampaign.activeDate.`as`("startDate"),
                        qCampaign.finishDate.`as`("finishDate"),
                        qCampaignDetails.applyDate.`as`("applyDate"),
                        qCampaign.totalRecruitCount.`as`("totalRecruitCount"),
                        Expressions.stringTemplate(
                            "case " +
                                    "when {0} = 'ACTIVE' AND {1} < {2} then '모집 진행' " +
                                    "when {0} = 'ACTIVE' AND {1} = {2} then '모집 완료' " +
                                    "when {0} = 'WAIT' then '캠페인 대기' " +
                                    "when {0} = 'PROGRESS' then '캠페인 진행' " +
                                    "when {0} = 'COMPLETE' then '캠페인 완료' " +
                                    "else '오류' end",
                            qCampaignDetails.sellerStatus,
                            qCampaignDetails.joinCount,
                            qCampaignDetails.recruitCount
                        ).`as`("sellerStatus"),
                        qCampaign.campaignPlatform.`as`("campaignPlatform"),
                        GroupBy.list(
                            Projections.fields(
                                AdminCampaignDetailResponseDTO.UserInfo::class.java,
                                qCampaignEnroll.id.`as`("campaignEnrollId"),
                                qUser.id.`as`("userId"),
                                qCampaignEnroll.enrollStatus.`as`("enrollStatus"),
                                qUserInfo.username?.`as`("username"),
                                qUserInfo.phone?.`as`("phone"),
                                qCampaignEnroll.orderImageUrl.`as`("orderUrl"),
                                qCampaignEnroll.reviewImgUrl.`as`("reviewUrl"),
                                qUserInfo.nvId?.`as`("nvId"),
                                qUserInfo.cpId?.`as`("cpId"),
                                qCampaignEnroll.orderStatus.`as`("orderStatus"),
                                qCampaignEnroll.reviewStatus.`as`("reviewStatus"),
                                Expressions.dateTimeTemplate(
                                    LocalDateTime::class.java,
                                    "coalesce({0},{1})",
                                    qCampaignEnroll.updateAt,
                                    qCampaignEnroll.createAt
                                ).`as`("updateAt")
                            )
                        ).`as`("userInfo")
                    )
                )
            ).firstOrNull()
    }

    /**
     *
     */
    @Transactional
    @Throws(CampaignException::class)
    fun setCampaignDetails(request: CampaignStartRequestDTO): Boolean {

        val campaignDetails = campaignDetailsRepository.findById(request.campaignDetailsId)
            .orElseThrow { throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST) }

        if ( campaignDetails.sellerStatus == SellerStatus.WAIT ) {
            if ( campaignDetails.campaign?.campaignStatus != CampaignStatus.PROGRESS ){
                campaignDetails.campaign?.campaignStatus = CampaignStatus.PROGRESS
                campaignDetails.campaign?.updateAt = LocalDateTime.now()
            }
            campaignDetails.sellerStatus = SellerStatus.ACTIVE
            campaignDetails.updateAt = LocalDateTime.now()
            this.campaignDetailsRepository.save(campaignDetails)

        }else {
            throw CampaignException(CampaignError.CAMPAIGN_STATUS_NOT_WAIT)
        }

        return true
    }

    /**
     * 캠페인 등록 정보 조회
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun getCampaign(campaignId: Long): GetCampaignResponseDTO? {

        val qCampaign = QCampaign.campaign
        val qCampaignOptions = QCampaignOptions.campaignOptions
        val qCampaignSubOption = QCampaignSubOptions.campaignSubOptions
        val qCampaignGuide = QCampaignGuide.campaignGuide

        val campaign = queryFactory
            .select(
                Projections.fields(
                    GetCampaignResponseDTO::class.java,
                    qCampaign.companyName.`as`("companyName"),
                    qCampaign.campaignPlatform.`as`("platform"),
                    qCampaign.campaignCategory.`as`("category"),
                    qCampaign.startTime.`as`("startTime"),
                    qCampaign.endTime.`as`("endTime"),
                    qCampaign.campaignTitle.`as`("productTitle"),
                    qCampaign.campaignImgUrl.`as`("campaignImgUrl"),
                    qCampaign.campaignUrl.`as`("campaignLink"),
                    qCampaign.campaignPrice.`as`("campaignPrice"),
                    qCampaign.reviewPoint.`as`("reviewPoint"),
                    qCampaign.campaignProgressPrice.`as`("progressPrice"),
                    qCampaign.activeDate.`as`("startSaleDateTime"),
                    qCampaign.finishDate.`as`("endSaleDateTime"),
                    qCampaign.sellerRequest.`as`("sellerRequest"),
                    Expressions.stringTemplate(
                        "글자 수: {0} \n" +
                                "강조 내용: {1}",
                        qCampaignGuide.textLength,
                        qCampaignGuide.strengthText,
                    ).`as`("sellerGuide")
                )
            )
            .from(qCampaign)
            .join(qCampaignGuide).on(qCampaign.eq(qCampaignGuide.campaign))
            .where(qCampaign.id.eq(campaignId))
            .fetchOne()

        campaign?.options =  queryFactory
            .select(
                Projections.constructor(
                    GetCampaignResponseDTO.Options::class.java,
                    qCampaignOptions.title.`as`("optionTitle"),
                    qCampaignOptions.recruitPeople.`as`("recruitPeople"),
                    Projections.list(
                        Projections.constructor(
                            GetCampaignResponseDTO.SubOptions::class.java,
                            qCampaignSubOption.title.`as`("subOptionTitle"),
                            qCampaignSubOption.addPrice.`as`("addPrice"),
                            qCampaignSubOption.recruitPeople.`as`("subRecruitPeople")
                        )
                    )
                )
            )
            .from(qCampaignOptions)
            .where(qCampaignOptions.campaign.id.eq(campaignId))
            .leftJoin(qCampaignSubOption).on(
                qCampaignSubOption.campaign.id.eq(campaignId)
                    .and(qCampaignSubOption.campaignOptions.eq(qCampaignOptions))
            )
            .fetch()

        return campaign
    }

    /**
     * 캠페인 셀러 이미지 검수 서비스
     * @param imageStatus: 리뷰 요청 카테고리
     * @return Boolean: 리뷰 확인 성공 유무
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun setCampaignImageStatus(imageStatus: ImageCheck, request: CampaignCheckRequestDTO): Boolean {

        // 캠페인 참여 정보 조회
        val enroll = campaignEnrollRepository.findById(request.campaignEnrollId)
            .orElseThrow { throw CampaignException(CampaignError.REGISTER_CAMPAIGN_NOT_EXIST) }

        // 캠페인 참여자와 등록된 사용자 동일 여부 확인
        if ( enroll.user?.id != request.userId ) {
            throw CampaignException(CampaignError.CAMPAIGN_ENROLL_NOT_MATCH)
        }

        // 이미지 카테고리 값에 따른 이미지 요청 상태값 변경 처리
        when (imageStatus) {
            ImageCheck.review -> { // 리뷰 이미지 통과 시,
                // 등록 리뷰이미지 상태 값 변경
                enroll.reviewStatus = ImageStatus.COMPLETE

                // 캠페인 참여 정보 등록 완료 처리
                enroll.enrollStatus = EnrollStatus.COMPLETE

                // 포인트 값 조회
                val pointValue = enroll.pointAttribute.filter {
                    it.status == PointStatus.EXPECT
                }.sumOf {
                    it.point
                }

                // 포인트 상태 값 완료 처리
                val pointAttributes = enroll.pointAttribute.map {
                    it.status = PointStatus.COMPLETE
                    it.updateAt = LocalDateTime.now()
                    it
                }.toMutableList()

                enroll.pointAttribute = pointAttributes

                val user = accountRepository.findById(request.userId)
                    .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

                val point = pointRepository.findByUser(user)
                    ?: throw PointException(PointError.POINT_IS_NOT_EXIST)

                point.remainPoint = point.remainPoint + pointValue
                point.expectPoint = point.expectPoint - pointValue
                point.updateAt = LocalDateTime.now()

                this.pointRepository.save(point)
            }

            ImageCheck.modify -> {
                enroll.reviewStatus = ImageStatus.EDIT
            }

            ImageCheck.order -> {
                enroll.orderStatus = ImageStatus.COMPLETE
                enroll.enrollStatus = EnrollStatus.REVIEW
            }

            ImageCheck.reorder -> {
                enroll.orderStatus = ImageStatus.EDIT
            }
        }

        enroll.updateAt = LocalDateTime.now()
        this.campaignEnrollRepository.save(enroll)

        return true
    }

    /**
     * 캠페인 참여자 시작 요청 서비스
     * @param request: StartCampaignUserRequestDTO
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun setCampaignStart(request: StartCampaignUserRequestDTO): Boolean {

        val campaignEnrolls = campaignEnrollRepository.findByIdIn(request.enrollIds)
            .filterNotNull()
            .filter{
                it.enrollStatus != EnrollStatus.CANCEL
            }

        if (campaignEnrolls.isEmpty()) return false

        val phoneNumber = campaignEnrolls.mapNotNull { it.user?.userInfo?.phone }

        campaignEnrolls.forEach { enroll ->
            if ( !enroll.cancelYn ) {
                enroll.enrollStatus = EnrollStatus.PROGRESS
                enroll.updateAt = LocalDateTime.now()
                campaignEnrollRepository.save(enroll)
            }
        }

        messageService.sendCampaignStart(campaignEnrolls.first().campaign!!,campaignEnrolls, phoneNumber)

        return true
    }

    @Transactional
    @Throws(CampaignException::class)
    fun startCampaign(request: CampaignEnrollPlayRequestDTO): Boolean {

        val campaignDetails = campaignDetailsRepository.findById(request.campaignDetailsId)
            .orElseThrow { throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST) }

        val enroll = campaignDetails.enroll.filter{
            it.enrollStatus != EnrollStatus.CANCEL
        }.map {
            it.enrollStatus = EnrollStatus.PROGRESS
            it.updateAt = LocalDateTime.now()
            it
        }

        val phone = campaignDetails.enroll.filter {
            it.enrollStatus != EnrollStatus.CANCEL
        }.mapNotNull {
            it.user?.userInfo?.phone
        }

        val campaignEnrolls = campaignDetails.enroll.filter {
            it.enrollStatus != EnrollStatus.CANCEL
        }

        messageService.sendCampaignStart(campaignDetails.campaign!!,campaignEnrolls, phone)

        campaignDetails.campaign.campaignStatus = CampaignStatus.PROGRESS
        campaignDetails.campaign.updateAt = LocalDateTime.now()
        campaignDetails.enroll = enroll.toMutableList()
        campaignDetails.sellerStatus = SellerStatus.PROGRESS
        campaignDetails.updateAt = LocalDateTime.now()
        this.campaignDetailsRepository.save(campaignDetails)

        return true
    }

    /**
     * 암호화 된 URL 생성
     * @param input: 암호화할 생성 값
     * @return 암호화 된 URLEncoding 값
     */
    @Transactional
    @Throws(CampaignException::class)
    fun generatedUrl(input: String): String {
        val stringToKey = AESEncryption.stringToKey(secretKey)
        val encrypt = AESEncryption.encrypt(input,stringToKey)
        return URLEncoder.encode(encrypt, StandardCharsets.UTF_8)
    }

    /**
     * 암호화 된 CD_IX 값 복호화
     * @param input: 암호화 된 값
     * @return 복호화 된 값
     */
    @Transactional
    @Throws(CampaignException::class)
    fun decryptEncodeUrl(input: String): String {
        val stringToKey = AESEncryption.stringToKey(secretKey)
        return AESEncryption.decrypt(input,stringToKey)
    }

    /**
     * 포인트 전환 내역 정보 조회
     * @return 포인트 전환 내역 조회
     */
    @Transactional
    @Throws(PointException::class)
    fun getExchangeConvertExcel(): List<PointExchangeResponseDTO> {
        val exchanges = pointExchangeRepository.findByStatusAndDownloadYn(ExchangeStatus.REQ, false)
        return exchanges.map {
            PointExchangeResponseDTO(
                pointExchangeId = it.id,
                bankCode = it.user?.userInfo?.bankCode,
                account = it.user?.userInfo?.account,
                pointValue = it.pointValue,
                name = it.user?.userInfo?.username,
                depositMemo = it.uniqueKey,
                withdrawMemo = "리바이럴 포인트"
            )
        }
    }

    /**
     * 포인트 전환 내역 엑셀 파일 다운로드 처리 서비스
     * @param request: 포인트 변환 요청
     * @return 다운로드 처리 완료 유무
     * @exception PointException
     */
    @Transactional
    @Throws(PointException::class)
    fun setPointExchangeDownload(request: PointExchangeIdsRequestDTO): Boolean {
        val pointExchanges = pointExchangeRepository.findAllById(request.pointExchangeIds)
        pointExchanges.forEach {
            it.downloadYn = true
            it.updateAt = LocalDateTime.now()
        }
        return true
    }

    @Transactional
    @Throws(PointException::class)
    fun setPointExchange(request: List<AccountResultDTO>): Boolean {
        val pointExchange = pointExchangeRepository.findByStatusAndDownloadYn(ExchangeStatus.REQ, true)
        val bankCodeList = pointExchange.associateBy { it.uniqueKey }

        request.map { account ->
            val matchedAccount = bankCodeList[account.uniqueKey]

            when {
                // 상태값이 오류이면서, 매칭되는 계좌값이 존재할 경우
                account.status == "오류" && matchedAccount != null -> {
                    // 전환 실패처리
                    matchedAccount.status = ExchangeStatus.FAIL
                    matchedAccount.exchangeDesc = "계좌번호 또는 예금주를 확인해주세요."
                    matchedAccount.updateAt = LocalDateTime.now()
                    pointExchangeRepository.save(matchedAccount)

                    val point = pointRepository.findByUser(matchedAccount.user!!)
                        ?:throw PointException(PointError.POINT_IS_NOT_EXIST)
                    point.remainPoint = point.remainPoint.plus(matchedAccount.pointValue)
                    point.updateAt = LocalDateTime.now()
                    pointRepository.save(point)
                }

                account.status == "정상" && matchedAccount != null -> {
                    matchedAccount.status = ExchangeStatus.FINISH
                    matchedAccount.exchangeDesc = "${matchedAccount.pointValue} 포인트 전환 완료"
                    matchedAccount.updateAt = LocalDateTime.now()
                    pointExchangeRepository.save(matchedAccount)

                    val point = pointRepository.findByUser(matchedAccount.user!!)
                        ?: throw PointException(PointError.POINT_IS_NOT_EXIST)
                    point.totalChangePoint = point.totalChangePoint + matchedAccount.pointValue
                    point.updateAt = LocalDateTime.now()
                    pointRepository.save(point)
                }

                else -> {
                    //TODO 예외 상황 추가 확인 시, 추가 처리
                }
            }
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
        return if ( startDate.isBefore(endDate) || startDate.isEqual(endDate) ) {
            ChronoUnit.DAYS.between(startDate,endDate)
        }else if( startDate.isEqual(endDate)) {
            0L
        }else{
            throw CampaignException(CampaignError.START_DATE_SET)
        }
    }
}
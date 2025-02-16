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
                    qCampaign.cpType.`as`("cpType")
                )
            )
            .from(qCampaign)
            .join(qCampaignDetails).on(
                qCampaignDetails.campaign.id.eq(qCampaign.id)
            ).where(
                qCampaignDetails.isDelete.eq(false)
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
                        qCampaignDetails.sellerStatus.eq(SellerStatus.ACTIVE)
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
                qCampaignDetails.sellerStatus.eq(SellerStatus.ACTIVE)
                    .and(qCampaignDetails.isDelete.eq(false))
                    .and(
                        qCampaignDetails.applyDate.between(now.minusDays(1),now.plusDays(1))
                    )
            ).or(
                qCampaignDetails.sellerStatus.eq(SellerStatus.WAIT)
                    .and(qCampaignDetails.isDelete.eq(false))
                    .and(
                        qCampaignDetails.applyDate.between(now.minusDays(1),now.plusDays(1))
                    )
            ).or(
                qCampaignDetails.sellerStatus.eq(SellerStatus.PROGRESS)
                    .and(qCampaignDetails.isDelete.eq(false))
                    .and(
                        qCampaignDetails.applyDate.between(now.minusDays(1),now.plusDays(1))
                    )
            )
            .or(
                qCampaignDetails.sellerStatus.eq(SellerStatus.COMPLETE)
                    .and(qCampaignDetails.isDelete.eq(false))
            )
        }

        // 순차정렬 enum 커스텀
        val order = CaseBuilder()
            .`when`(qCampaignDetails.sellerStatus.eq(SellerStatus.ACTIVE)).then(0)
            .`when`(qCampaignDetails.sellerStatus.eq(SellerStatus.WAIT)).then(1)
            .`when`(qCampaignDetails.sellerStatus.eq(SellerStatus.PROGRESS)).then(2)
            .`when`(qCampaignDetails.sellerStatus.eq(SellerStatus.COMPLETE)).then(3)
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
    fun getCampaignDetail(campaignDetailsId: Long): CampaignDetailResponseDTO {

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
                    qCampaign.cpType.`as`("cpType"),
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
                    cpType = campaign.cpType,
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

        // 캠페인 상세정보가 아직 게시전이라면
        if ( campaignDetails.sellerStatus == SellerStatus.WAIT ) {
            throw CampaignException(CampaignError.CAMPAIGN_STATUS_WAIT)
        }

        if ( campaignDetails.sellerStatus == SellerStatus.PROGRESS ) {
            throw CampaignException(CampaignError.CAMPAIGN_STATUS_PROGRESS)
        }

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
        if ( campaign.isNotDuplicated ) {
            throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_DUPLICATED)
        }

        // 캠페인 참여 인원이 꽉 찼을 경우,
//        if ( campaignDetails.joinCount >= campaignDetails.recruitCount ) {
//            applicationEventPublisher.publishEvent(
//                CampaignDetailsEventListener.CampaignDetailsUpdateEvent(campaignDetails)
//            )
//            throw CampaignException(CampaignError.CAMPAIGN_FULL_RECRUIT)
//        }

        // 사용자의 플랫폼의 아이디가 존재하지 않는다면 오류
        when (campaign.campaignPlatform) {
            CampaignPlatform.COUPANG -> {
                if ( user?.userInfo?.cpId == null ) {
                    throw CampaignException(CampaignError.CAMPAIGN_CP_ID_NULL)
                }
            }
            CampaignPlatform.NAVER -> {
                if (user?.userInfo?.nvId == null) throw CampaignException(CampaignError.CAMPAIGN_NV_ID_NULL)
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

            if ( option.subOptions!!.none { it.id == request.campaignSubOptionId } ) {
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
                reviewStatus = ImageStatus.REGISTER,
                orderStatus = ImageStatus.REGISTER,
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
                campaignEnrollId = it?.id,
                campaignStatus = it?.enrollStatus,
                registerDate = it?.createAt,
                campaignImgUrl = it?.campaign?.campaignImgUrl,
                campaignTitle = it?.campaign?.campaignTitle,
                campaignLink = it?.campaign?.campaignUrl,
                sellerGuide =
                "<p> 옵션 1 : ${it?.options?.title ?: "없음" }</p>"+
                "<p> 옵션 2 : ${it?.subOptions?.title ?: "없음" }</p><br><br>"+
                "<p> 1. 글자 제한: ${it?.campaign?.guide?.textLength ?: "없음"}</p>" +
                "<p> 2. 등록 조건: ${it?.campaign?.guide?.condition ?: "없음"}</p>" +
                "<p> 3. 기타 사항: ${it?.campaign?.guide?.etcText ?: "없음"}</p> <br>" +
                "<p> 4. 강조 내용 <br>${it?.campaign?.guide?.strengthText ?: "없음"}</p>",
                orderStatus = it?.orderStatus,
                reviewStatus = it?.reviewStatus
            )
        }

        val isHavePoint = pointRepository.existsByUser(user)
        val point = if ( !isHavePoint ) {
            this.pointRepository.save(Point(
                user = user,
                remainPoint = 0,
                expectPoint = 0,
                totalChangePoint = 0,
                createAt = LocalDateTime.now()
            ))
        }else {
            this.pointRepository.findByUser(user)
                ?: throw PointException(PointError.POINT_IS_NOT_EXIST)
        }

        val exchange = pointExchangeRepository.findByUserAndStatus(user, ExchangeStatus.REQ)

        val myCampaignUserInfo = MyCampaignResponseDTO.MyCampaignUserInfo(
            name = user?.userInfo?.username,
            loginId = user.email,
            expectPoint = point.expectPoint,
            totalPoint = point.totalChangePoint,
            remainPoint = point.remainPoint,
            exchangePoint = exchange?.filter { it?.status == ExchangeStatus.REQ }?.sumOf{ it?.pointValue ?: 0 }
        )

        return MyCampaignResponseDTO(
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

        if ( enroll.enrollStatus == EnrollStatus.REVIEW ) {
            // 캠페인 정보 조회 (일련번호 디렉토리 값으로 만들기 위해 조회)
            val campaign = enroll.campaign
                ?: throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST)

            // AmazonS3 이미지 파일 업로드
            val fileImgUrl = amazonS3Service.uploadMultipartFile(image, "${campaign.id}/review")

            enroll.reviewImgUrl = fileImgUrl.imageUrl
            enroll.reviewStatus = ImageStatus.INSPECT
            enroll.updateAt = LocalDateTime.now()
            this.campaignEnrollRepository.save(enroll)
        }

        return true
    }

    /**
     * 주문번호 이미지 등록 서비스
     * @param request: EnrollProductOrderNoRequestDTO
     * @return Boolean
     * @exception CampaignException
     */
    @Transactional
    @Throws(CampaignException::class)
    fun setOrderImage(request: EnrollReviewRequestDTO, image: MultipartFile): Boolean {

        // 캠페인 참여 정보 조회
        val enroll = campaignEnrollRepository.findById(request.campaignEnrollId)
            .orElseThrow { throw CampaignException(CampaignError.REGISTER_CAMPAIGN_NOT_EXIST) }

        // 캠페인 참여자와 요청한 유저의 아이디가 동일하지 않을 경우 오류 발생
        if ( enroll.user?.id != request.userId ) {
            throw CampaignException(CampaignError.REGISTER_CAMPAIGN_NOT_EXIST)
        }

        // 캠페인 참여 상태가 진행(PROGRESS)일 경우
        if ( enroll.enrollStatus == EnrollStatus.PROGRESS ) {

            val campaign = enroll.campaign
                ?: throw CampaignException(CampaignError.CAMPAIGN_IS_NOT_EXIST)

            // AmazonS3 이미지 파일 업로드
            val fileImgUrl = amazonS3Service.uploadMultipartFile(image, "${campaign.id}/order")

            // 해당 상태가 이미지 재검수 요청이라면
            enroll.orderImageUrl = fileImgUrl.imageUrl
            enroll.orderStatus = ImageStatus.INSPECT
            enroll.updateAt = LocalDateTime.now()
            this.campaignEnrollRepository.save(enroll)

            return true
        }else {
            throw CampaignException(CampaignError.CAMPAIGN_ORDER_NO)
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

            val campaignDetails = enroll.campaignDetails

            campaignDetails.joinCount = if ( (campaignDetails.joinCount - 1) <= 0 ) 0 else {
                campaignDetails.joinCount - 1
            }

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


}
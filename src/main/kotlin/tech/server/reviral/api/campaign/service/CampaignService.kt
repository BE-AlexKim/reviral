package tech.server.reviral.api.campaign.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.campaign.model.dto.SaveCampaignRequestDTO
import tech.server.reviral.api.campaign.model.entity.Campaign
import tech.server.reviral.api.campaign.model.entity.CampaignDetails
import tech.server.reviral.api.campaign.model.entity.CampaignOptions
import tech.server.reviral.api.campaign.model.entity.CampaignSubOptions
import tech.server.reviral.api.campaign.model.enums.CampaignStatus
import tech.server.reviral.api.campaign.model.enums.OptionType
import tech.server.reviral.api.campaign.repository.CampaignDetailsRepository
import tech.server.reviral.api.campaign.repository.CampaignOptionsRepository
import tech.server.reviral.api.campaign.repository.CampaignRepository
import tech.server.reviral.api.campaign.repository.CampaignSubOptionsRepository
import tech.server.reviral.api.campaign.repository.specification.CampaignSpecification
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
    private val campaignSubOptionsRepository: CampaignSubOptionsRepository
){


    /**
     * 구매 캠페인 정보 조회
     */
    @Transactional
    @Throws(CampaignException::class)
    fun searchCampaigns(
        category: String?,
        platform: String?,
        status: String?,
        campaignId: Long?,
        pageable: Pageable
    ): Page<Campaign> {

        when(category) {
            // 오늘 오픈한 캠페인 목록 조회
            "today" -> {
                var spec = Specification.where(CampaignSpecification.betweenActiveDate())

                if ( platform != null ) {
                    spec = spec.and(CampaignSpecification.platformContains(platform))
                }

                if ( status != null ) {
                    spec = spec.and(CampaignSpecification.equalCampaignStatus(status))
                }
                return campaignRepository.findAll(spec, pageable)
            }
            // 마감임박 캠페인 목록 조회
            "deadline" -> {
                var spec = Specification.where(CampaignSpecification.betweenFinishDate())

                if ( platform != null ) {
                    spec = spec.and(CampaignSpecification.platformContains(platform))
                }

                if ( status != null ) {
                    spec = spec.and(CampaignSpecification.equalCampaignStatus(status))
                }
                return campaignRepository.findAll(spec, pageable)
            }
            "time","daily" -> {
                var spec = Specification.where(CampaignSpecification.equalCampaignCategory(category))
                if ( platform != null ) {
                    spec = spec.and(CampaignSpecification.platformContains(platform))
                }

                if ( status != null ) {
                    spec = spec.and(CampaignSpecification.equalCampaignStatus(status))
                }
                return campaignRepository.findAll(spec, pageable)
            }
            else -> {
                throw CampaignException("카테고리에 정보가 없습니다.")
            }
        }
    }

    /**
     * 당일구매 캠페인 목록 정보 조회
     */
    @Transactional
    @Throws(CampaignException::class)
    fun getDailyCampaigns() {

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
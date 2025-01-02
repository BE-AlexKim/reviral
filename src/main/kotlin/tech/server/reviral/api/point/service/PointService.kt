package tech.server.reviral.api.point.service

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.Basic
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.account.model.entity.QUser
import tech.server.reviral.api.account.repository.AccountRepository
import tech.server.reviral.api.campaign.model.entity.QCampaign
import tech.server.reviral.api.campaign.model.entity.QCampaignDetails
import tech.server.reviral.api.point.model.dto.ExchangePointRequestDTO
import tech.server.reviral.api.point.model.dto.PointAttributeResponseDTO
import tech.server.reviral.api.point.model.entity.PointExchange
import tech.server.reviral.api.point.model.entity.QPointAttribute
import tech.server.reviral.api.point.model.entity.QPointExchange
import tech.server.reviral.api.point.model.enums.ExchangeStatus
import tech.server.reviral.api.point.model.enums.PointStatus
import tech.server.reviral.api.point.repository.PointAttributeRepository
import tech.server.reviral.api.point.repository.PointExchangeRepository
import tech.server.reviral.api.point.repository.PointRepository
import tech.server.reviral.common.config.response.exception.BasicException
import tech.server.reviral.common.config.response.exception.PointException
import tech.server.reviral.common.config.response.exception.enums.BasicError
import tech.server.reviral.common.config.response.exception.enums.PointError
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.point.service
 * fileName       : PointService
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
@Service
class PointService constructor(
    private val pointRepository: PointRepository,
    private val pointAttributeRepository: PointAttributeRepository,
    private val pointExchangeRepository: PointExchangeRepository,
    private val accountRepository: AccountRepository,
    private val queryFactory: JPAQueryFactory
) {

    /**
     * 포인트 전환 요청 서비스
     * @param request: ExchangePointRequestDTO
     * @return Boolean
     */
    @Transactional
    @Throws(PointException::class, BasicException::class)
    fun setExchangePoint(request: ExchangePointRequestDTO): Boolean {

        val user = accountRepository.findById(request.userId)
            .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

        // 포인트 내역 조회
        val point = pointRepository.findByUser(user)
            ?: throw PointException(PointError.POINT_IS_NOT_EXIST)

        // 기존 전환 신청 내역이 있는지 조회
        val pointExchanges = pointExchangeRepository.findByUserAndStatus(user, ExchangeStatus.REQ)

        // 기존 전환 신청 내역이 있을 경우,
        val totalPoint = point.remainPoint + pointExchanges.filterNotNull()
            .sumOf { it.pointValue }

        // 전환 가능한 포인트 내역을 초과했을 경우, 오류 발생
        if ( totalPoint >= request.exchangeValue ) {
            throw PointException(PointError.EXCHANGE_BETTER_THAN_REMAIN)
        }

        // 포인트 전환 내역 정보 등록
        pointExchangeRepository.save(PointExchange(
            user = user,
            status = ExchangeStatus.REQ,
            pointValue = request.exchangeValue,
            exchangeDesc = ExchangeStatus.REQ.description(),
            createAt = LocalDateTime.now()
        ))

        return true
    }

    @Transactional
    @Throws(PointException::class, BasicException::class)
    fun getPointAttributes(loginId: String): PointAttributeResponseDTO {
        val user = accountRepository.findByLoginId(loginId)
            ?: throw BasicException(BasicError.USER_NOT_EXIST)

        val point = pointRepository.findByUser(user)
            ?: throw PointException(PointError.POINT_IS_NOT_EXIST)

        val userInfo = PointAttributeResponseDTO.UserInfo(
            name = user.name,
            loginId = user.loginId,
            expectPoint = point.expectPoint,
            totalPoint = point.totalChangePoint,
            remainPoint = point.remainPoint
        )

        val pointExchange = pointExchangeRepository.findByUserOrderByCreateAt(user)
        if ( pointExchange != null) {
            return PointAttributeResponseDTO(
                user = userInfo,
                pointHistory = pointExchange.map {
                    PointAttributeResponseDTO.PointAttr(
                        pointStatus = it.status!!,
                        createAt = it.createAt!!,
                        exchangeDesc = it.exchangeDesc!!
                    )
                }
            )
        }else {
            return PointAttributeResponseDTO(
                user = userInfo,
                pointHistory = null
            )
        }
    }
}
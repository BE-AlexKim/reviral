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

        // 포인트 전환 내역 조회
        val point = pointRepository.findByUser(user)
            ?: throw PointException(PointError.POINT_IS_NOT_EXIST)

        // 전환 가능한 포인트 내역을 초과했을 경우, 오류 발생
        if ( point.remainPoint >= request.exchangeValue ) {
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
    fun getPointAttributes(loginId: String) {
        val user = accountRepository.findByLoginId(loginId)
            ?: throw BasicException(BasicError.USER_NOT_EXIST)

        val qUser = QUser.user
        val qPointAttribute = QPointAttribute.pointAttribute
        val qPointExchange = QPointExchange.pointExchange

        val query = queryFactory
            .select(
            )
            .from(qPointExchange)
            .leftJoin(qPointExchange.user, qUser)
            .on(qPointAttribute.user.id.eq(qUser.id))
            .leftJoin(qPointAttribute.user, qUser)
            .on(qPointAttribute.user.id.eq(qUser.id))
            .where(
                qUser.id.eq(user.id)
                    .and(qPointAttribute.status.eq(PointStatus.COMPLETE))
            )
            .orderBy(
                qPointExchange.createAt.desc().nullsLast(),
                qPointAttribute.createAt.desc().nullsLast()
            ).fetch()

    }
}
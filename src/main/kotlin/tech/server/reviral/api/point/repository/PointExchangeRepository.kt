package tech.server.reviral.api.point.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.point.model.entity.PointExchange
import tech.server.reviral.api.point.model.enums.ExchangeStatus

/**
 *packageName    : tech.server.reviral.api.point.repository
 * fileName       : PointExchangeRepository
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
@Repository
interface PointExchangeRepository: JpaRepository<PointExchange, Long> {

    fun findByUserAndStatus(user: User, status: ExchangeStatus): MutableList<PointExchange?>

    fun findByUserOrderByCreateAt(user: User): MutableList<PointExchange>?
}
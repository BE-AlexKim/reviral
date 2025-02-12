package tech.server.reviral.api.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.account.model.entity.UserInfo

/**
 *packageName    : tech.server.reviral.api.account.repository
 * fileName       : UserInfoRepository
 * author         : joy58
 * date           : 2025-02-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-06        joy58       최초 생성
 */
@Repository
interface UserInfoRepository: JpaRepository<UserInfo, Long> {

    fun existsByUser(user: User): Boolean

    fun findByUser(user: User): UserInfo

    fun findByPhoneIn(phones: List<String>): List<UserInfo>
}
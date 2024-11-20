package tech.server.reviral.api.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.server.reviral.api.account.model.entity.User

/**
 *packageName    : tech.server.reviral.api.account.repository
 * fileName       : AccountRepository
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@Repository
interface AccountRepository: JpaRepository<User, Long> {

    fun findByLoginId(loginId: String?): User?

}
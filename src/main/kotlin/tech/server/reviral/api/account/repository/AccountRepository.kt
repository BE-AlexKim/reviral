package tech.server.reviral.api.account.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.account.model.enums.Registration
import tech.server.reviral.api.account.model.enums.UserRole

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

    fun findByEmailAndRegistration(email: String?, registration: Registration): User?

    fun existsUserByEmail(email: String): Boolean

    fun findBySidAndRegistration(sid: String, registration: Registration): User

    fun existsBySidAndRegistration(sid: String, registration: Registration): Boolean

    fun existsBySid(sid: String): Boolean

    fun findByAuth(auth: UserRole, pageable: Pageable): Page<User>

}
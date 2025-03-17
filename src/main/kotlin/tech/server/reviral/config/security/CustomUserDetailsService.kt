package tech.server.reviral.config.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.account.repository.AccountRepository
import tech.server.reviral.config.response.exception.BasicException
import tech.server.reviral.config.response.exception.enums.BasicError

/**
 *packageName    : tech.server.reviral.common.config.security
 * fileName       : CustomUserDetailsService
 * author         : joy58
 * date           : 2024-11-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-21        joy58       최초 생성
 */
@Service
class CustomUserDetailsService(
    private val accountRepository: AccountRepository,
    private val jwtTokenHelper: JwtTokenHelper
): UserDetailsService {

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(token: String): UserDetails? {
        try {
            val claims = jwtTokenHelper.decryptClaims(token)
            val username = claims.subject.toLong()
            val user = accountRepository.findById(username)
            return if ( user.isEmpty ) {
                throw BasicException(BasicError.USER_NOT_MATCH)
            }else {
                user.get()
            }
        }catch ( e: UsernameNotFoundException) {
            throw UsernameNotFoundException("ERROR :: User not found.")
        }
    }
}
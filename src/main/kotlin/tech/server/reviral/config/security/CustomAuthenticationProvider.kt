package tech.server.reviral.config.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.account.model.enums.Registration
import tech.server.reviral.api.account.service.AccountService
import tech.server.reviral.config.response.exception.BasicException
import tech.server.reviral.config.response.exception.enums.BasicError

/**
 *packageName    : tech.server.reviral.common.config.security
 * fileName       : CustomAuthenticationProvider
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
@Component
class CustomAuthenticationProvider(
    private val passwordEncoder: PasswordEncoder,
    private val accountService: AccountService
): AuthenticationProvider {

    @Transactional
    @Throws(BasicException::class)
    override fun authenticate(authentication: Authentication?): Authentication {
        val username = authentication?.name?.toLong()!!
        val password: String = authentication.credentials.toString()

        val user = accountService.loadUserByUsername(username)

        // 사용자 탈퇴된 계정이라면
        if ( !user.isAccountNonExpired ) {
            throw BasicException(BasicError.BLACK_LIST_USER)
        }
        if ( user.registration == Registration.LOCAL ) {
            if ( !passwordEncoder.matches(password, user.userPassword ) ) {
                throw BasicException(BasicError.USER_CREDENTIALS_NOT_MATCH)
            }
        }

        return UsernamePasswordAuthenticationToken(user, password, user.authorities )
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}
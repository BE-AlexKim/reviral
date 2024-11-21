package tech.server.reviral.api.account.service

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.account.model.dto.SignInRequestDTO
import tech.server.reviral.api.account.model.dto.SignUpRequestDTO
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.account.repository.AccountRepository
import tech.server.reviral.common.config.response.exception.BasicException
import tech.server.reviral.common.config.response.exception.enums.BasicError
import tech.server.reviral.common.config.security.JwtToken
import tech.server.reviral.common.config.security.JwtTokenProvider

/**
 * packageName    : tech.server.reviral.api.account.service
 * fileName       : AccountService
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy585       최초 생성
 */
@Service
class AccountService constructor(
    private val accountRepository: AccountRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    @Throws(BasicException::class)
    fun loadUserByUsername(username: String?): User {
        return accountRepository.findByLoginId(username)
            ?: throw BasicException(BasicError.USER_NOT_EXIST)
    }

    @Transactional
    @Throws(Exception::class)
    fun getJwtToken(request: SignInRequestDTO): JwtToken {

        // UsernamePasswordAuthenticationToken 발급
        val authenticationToken = UsernamePasswordAuthenticationToken(request.loginId, request.password )
        // 인증 객체 생성
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        // 인증 객체에서 사용자 정보 객체로 변환
        val user = authentication.principal as User
        // 사용자 정보 객체로 변환 후, 토큰 생성
        return jwtTokenProvider.getJwtToken(user)

    }

    @Transactional
    @Throws(Exception::class)
    fun signUp( request: SignUpRequestDTO ): Boolean {
        accountRepository.save(
            User(
                loginId = request.loginId,
                loginPw = passwordEncoder.encode(request.loginPw),
                pointPw = passwordEncoder.encode(request.pointPw),
                address = request.address,
                phone = request.phoneNumber,
                auth = request.userRole
            )
        )
        return true
    }
}
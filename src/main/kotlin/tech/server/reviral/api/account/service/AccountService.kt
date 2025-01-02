package tech.server.reviral.api.account.service

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.account.model.dto.ReloadRequestDTO
import tech.server.reviral.api.account.model.dto.SignInRequestDTO
import tech.server.reviral.api.account.model.dto.SignUpRequestDTO
import tech.server.reviral.api.account.model.dto.UserInfoResponseDTO
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.account.model.enums.UserRole
import tech.server.reviral.api.account.repository.AccountRepository
import tech.server.reviral.common.config.response.exception.BasicException
import tech.server.reviral.common.config.response.exception.enums.BasicError
import tech.server.reviral.common.config.security.JwtRedisRepository
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
    private val passwordEncoder: PasswordEncoder,
    private val jwtRedisRepository: JwtRedisRepository
) {

    @Transactional
    @Throws(BasicException::class)
    fun loadUserByUsername(username: String?): User {
        return accountRepository.findByLoginId(username)
            ?: throw BasicException(BasicError.USER_NOT_EXIST)
    }

    /**
     * 아이디 중복체크 서비스
     * @param loginId
     * @return Boolean
     */
    @Transactional
    @Throws(BasicException::class)
    fun isLoginIdDuplicated(loginId: String): Boolean {
        return accountRepository.existsUserByLoginId(loginId)
    }

    @Transactional
    @Throws(BasicException::class)
    fun getUserInfo(username: String): UserInfoResponseDTO {
        val user = accountRepository.findByLoginId(username)
            ?: throw BasicException(BasicError.USER_NOT_EXIST)

        val phoneRegex = Regex("(\\d{3})(\\d{4})(\\d{4})")

        val phoneNumber = phoneRegex.replace(user.phone) { matchResult ->
            val ( part1, part2, part3 ) = matchResult.destructured
            "${part1}-${part2}-${part3[0]}***"
        }

        val accountNumber = user.account?.substring(0, user.account.lastIndex - 4)
        accountNumber?.plus("*".repeat(5))

        return UserInfoResponseDTO(
            phoneNumber = phoneNumber,
            address = user.address,
            bankCode = user.bankCode,
            accountNumber = accountNumber
        )
    }

    /**
     * 로그인 서비스
     * @param request SignInRequestDTO
     * @return JwtToken
     */
    @Transactional
    @Throws(Exception::class)
    fun signIn(request: SignInRequestDTO): JwtToken {

        // UsernamePasswordAuthenticationToken 발급
        val authenticationToken = UsernamePasswordAuthenticationToken(request.loginId, request.password )
        // 인증 객체 생성
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        // 인증 객체에서 사용자 정보 객체로 변환
        val user = authentication.principal as User
        // 사용자 정보 객체로 변환 후, 토큰 생성
        return jwtTokenProvider.getJwtToken(user)
    }

    /**
     * 리뷰어 회원가입 서비스
     * @param request: SignUpRequestDTO
     * @return Boolean
     */
    @Transactional
    @Throws(Exception::class)
    fun signUp( request: SignUpRequestDTO ): Boolean {

        if ( !isLoginIdDuplicated(request.loginId) ) { // 중복아이디가 존재하지 않을 경우
            accountRepository.save(
                User(
                    loginId = request.loginId,
                    loginPw = passwordEncoder.encode(request.loginPw),
                    gender = request.gender,
                    name = request.username,
                    address = request.address,
                    phone = request.phoneNumber,
                    auth = UserRole.ROLE_REVIEWER,
                    nvId = request.nvId,
                    cpId = request.cpId,
                    isEvent = request.isEvent
                )
            )
            return true
        }else {
            throw BasicException(BasicError.USER_ALREADY_EXIST)
        }
    }

    /**
     * 토큰 재발급 서비스
     * @param accessToken: String,
     * @param enteredRefreshToken: String
     * @return JwtToken
     */
    @Transactional
    @Throws(BasicException::class)
    fun reloadUserByRefreshToken( request: ReloadRequestDTO ): JwtToken {
        val isEnableAccessToken = jwtTokenProvider.decryptClaims(request.accessToken)
        if ( isEnableAccessToken.isEmpty ) { // 공백일 경우
            throw BasicException(BasicError.TOKEN_ERROR)
        }else {
            val claims = isEnableAccessToken.get() // Claims 정보 조회
            val cachedRefreshToken = jwtRedisRepository.get(claims.subject)
                ?: throw BasicException(BasicError.EXPIRED_REFRESH_TOKEN)

            if (cachedRefreshToken == request.refreshToken) { // 재발급 토큰 비교값이 동일할 경우
                val username = claims["loginId"].toString()
                return jwtTokenProvider.getJwtToken(loadUserByUsername(username))
            }else {
                throw BasicException(BasicError.TOKEN_NOT_MATCH)
            }
        }
    }
}
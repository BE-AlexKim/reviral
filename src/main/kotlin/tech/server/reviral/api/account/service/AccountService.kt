package tech.server.reviral.api.account.service

import jakarta.persistence.Basic
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.account.model.dto.*
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.account.model.enums.UserRole
import tech.server.reviral.api.account.repository.AccountRepository
import tech.server.reviral.common.config.mail.EmailService
import tech.server.reviral.common.config.mail.EmailTemplate
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
    private val jwtRedisRepository: JwtRedisRepository,
    private val emailService: EmailService
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

    /**
     * 사용자 설정 정보 조회
     * @param username: String
     * @return UserInfoResponseDTO
     */
    @Transactional
    @Throws(BasicException::class)
    fun getUserInfo(userId: Long): UserInfoResponseDTO {
        val user = accountRepository.findById(userId)
            .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

        val phoneRegex = Regex("(\\d{3})(\\d{4})(\\d{4})")

        val phoneNumber = phoneRegex.replace(user.phone) { matchResult ->
            val ( part1, part2, part3 ) = matchResult.destructured
            "${part1}-${part2}-${part3[0]}***"
        }

        val accountNumber = user.account?.substring(0, user.account?.lastIndex?.minus(4) ?: 0)
        accountNumber?.plus("*".repeat(5))

        return UserInfoResponseDTO(
            name = user.name,
            loginId = user.loginId,
            nvId = user.nvId,
            cpId = user.cpId,
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
     * 인증코드 발송
     * @param request: EmailAuthorizedRequestDTO
     * @return Boolean
     * @exception RuntimeException
     */
    @Transactional
    @Throws(RuntimeException::class)
    fun sendAuthorizedToEmail( type: String, request: EmailAuthorizedRequestDTO ): Boolean {

        if ( EmailTemplate.values().none { it.name == type.uppercase() } ) {
            throw BasicException(BasicError.UNSUPPORTED_URL)
        }

        val emailTemplate = EmailTemplate.valueOf(type.uppercase())

        // 템플릿 Context 변수 설정
        val values = emailTemplate.values(request.email)
        // 이메일 전송
        emailService.send(request.email, emailTemplate.getSubject(), emailTemplate.template(), values)
        // Redis 저장
        jwtRedisRepository.setAuthorizationCode("${emailTemplate.name}_${request.email}",  values["code"]!!, 300000)

        return true
    }

    /**
     * 인증코드 검사
     * @param request: AuthorizeCodeRequestDTO
     * @return Boolean
     * @exception BasicException
     */
    @Transactional
    @Throws(BasicException::class)
    fun verifyAuthorizedEmailCode(type: String, request: AuthorizeCodeRequestDTO ): Boolean {

        if ( EmailTemplate.values().none { it.name == type.uppercase() } ) {
            throw BasicException(BasicError.UNSUPPORTED_URL)
        }

        val emailTemplate = EmailTemplate.valueOf(type.uppercase())

        val code = jwtRedisRepository.getAuthorizationCode("${emailTemplate.name}_${request.email}")
            ?: throw BasicException(BasicError.AUTHORIZED_EMAIL)

        return if ( code != request.code ) {
            false
        }else {
            jwtRedisRepository.deleteAuthorizationCode("${emailTemplate.name}_${request.email}")
            true
        }
    }

    /**
     * 로그아웃 서비스
     * @param userId
     */
    @Transactional
    @Throws(RuntimeException::class)
    fun logout(userId: Long) {
        jwtRedisRepository.delete(userId.toString())
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
     * 사용자 개인정보 업데이트 서비스
     * @param request: UpdateUserInfoRequestDTO
     * @return Boolean 업데이트 유무
     * @exception BasicException
     */
    @Transactional
    @Throws(BasicException::class)
    fun updateUserInfo(request: UpdateUserInfoRequestDTO): Boolean {
        val user = accountRepository.findById(request.userId)
            .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

        request.phoneNumber?.let { user.phone = it }
        request.address?.let { user.address = it }
        request.bankCode?.let { user.bankCode = it }
        request.accountNumber?.let { user.account = it }
        request.nvId?.let { user.nvId = it }
        request.cpId?.let { user.cpId = it }
        request.loginPw?.let { user.loginPw = passwordEncoder.encode(it) }
        request.pointPw?.let { user.pointPw = passwordEncoder.encode(it) }

        accountRepository.save(user)
        return true
    }

    /**
     * 비밀번호 검증 서비스
     * @param request: ValidationPasswordRequestDTO
     * @return 비밀번호 검증여부
     * @exception BasicException
     */
    @Transactional
    @Throws(BasicException::class)
    fun isValidPassword(request: ValidationPasswordRequestDTO, type: String): Boolean {
        val user = accountRepository.findById(request.userId)
            .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

        val password = when(type) {
            "loginPw" -> {
                user.loginPw
            }
            "pointPw" -> {
                user.pointPw ?: throw BasicException(BasicError.POINT_PASSWORD_SET)
            }
            else -> throw BasicException(BasicError.UNSUPPORTED_URL)
        }

        return passwordEncoder.matches(request.password,password)
    }

    /**
     * 토큰 재발급 서비스
     * @param request: ReloadRequestDTO
     * @return JwtToken
     */
    @Transactional
    @Throws(BasicException::class)
    fun reloadUserByRefreshToken( request: ReloadRequestDTO ): JwtToken {

        val userId = jwtRedisRepository.get(request.refreshToken)
            ?: throw BasicException(BasicError.EXPIRED_REFRESH_TOKEN)

        val claim = jwtTokenProvider.getClaim(request.refreshToken)

        if (userId == claim.sub && claim.iss == "Reviral") { // 재발급 토큰 비교값이 동일할 경우
            val username = claim.username
            val token = jwtTokenProvider.getJwtToken(loadUserByUsername(username))
            jwtRedisRepository.delete(request.refreshToken)
            return token
        }else {
            throw BasicException(BasicError.TOKEN_NOT_MATCH)
        }
    }

}
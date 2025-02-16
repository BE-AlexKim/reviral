package tech.server.reviral.api.bussiness.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.account.model.dto.SignInRequestDTO
import tech.server.reviral.api.account.model.dto.UserInfoResponseDTO
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.account.model.enums.Registration
import tech.server.reviral.api.account.model.enums.UserRole
import tech.server.reviral.api.account.repository.AccountRepository
import tech.server.reviral.api.bussiness.model.dto.AdminSignupRequestDTO
import tech.server.reviral.api.bussiness.model.dto.ReviewerInfoResponseDTO
import tech.server.reviral.common.config.response.exception.BasicException
import tech.server.reviral.common.config.response.exception.enums.BasicError
import tech.server.reviral.common.config.security.JwtToken
import tech.server.reviral.common.config.security.JwtTokenProvider
import java.util.*

/**
 *packageName    : tech.server.reviral.api.bussiness.service
 * fileName       : SellerAccountService
 * author         : joy58
 * date           : 2025-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-13        joy58       최초 생성
 */
@Service
class SellerAccountService constructor(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider
){

    @Transactional
    @Throws(BasicException::class)
    fun getReviewers(pageable: PageRequest): Page<ReviewerInfoResponseDTO> {
        return accountRepository.findByAuth(UserRole.ROLE_REVIEWER, pageable)
            .map {
                ReviewerInfoResponseDTO(
                    userId = it.id!!,
                    name = it.userInfo?.username ?: "없음",
                    loginId = it?.email ?: "없음",
                    phoneNumber = it?.userInfo?.phone?.replace(Regex("(\\d{3})(\\d{4})(\\d{4})"), "$1-$2-$3") ?: "없음",
                    nvId = it?.userInfo?.nvId ?: "없음",
                    cpId = it?.userInfo?.cpId ?: "없음",
                    address = it?.userInfo?.address ?: "없음",
                    bankCode = it?.userInfo?.bankCode ?: "없음",
                    accountNumber = it?.userInfo?.account ?: "없음",
                )
            }
    }

    /**
     * 어드민 회원가입
     * @param request: AdminSignUpRequestDTO
     * @return 가입여부
     */
    @Transactional
    @Throws(BasicException::class)
    fun signup(request: AdminSignupRequestDTO): Boolean {
        accountRepository.save(
            User(
            registration = Registration.LOCAL,
            email = request.loginId,
            userPassword = passwordEncoder.encode(request.loginPw),
            sid = UUID.randomUUID().toString(),
            auth = UserRole.ROLE_ADMIN,
        ))
        return true
    }

    /**
     * 어드민 계정 로그인
     * @param request: SignInRequestDTO
     * @return JwtToken: 토큰객체
     * @exception BasicException
     */
    @Transactional
    @Throws(BasicException::class)
    fun login(request: SignInRequestDTO): JwtToken {
        val user = accountRepository.findByEmailAndRegistration(request.loginId, Registration.LOCAL)
            ?: throw BasicException(BasicError.USER_NOT_EXIST)

        val authenticationToken = UsernamePasswordAuthenticationToken( user.id, request.password )

        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        val account = authentication.principal as User

        return jwtTokenProvider.getJwtToken(account)

    }


}
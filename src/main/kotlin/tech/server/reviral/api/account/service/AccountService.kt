package tech.server.reviral.api.account.service

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.account.model.dto.*
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.account.model.entity.UserInfo
import tech.server.reviral.api.account.model.enums.Registration
import tech.server.reviral.api.account.model.enums.UserRole
import tech.server.reviral.api.account.repository.AccountRepository
import tech.server.reviral.api.account.repository.UserInfoRepository
import tech.server.reviral.api.oauth.model.OAuthUserInfo
import tech.server.reviral.api.point.model.entity.Point
import tech.server.reviral.api.point.repository.PointRepository
import tech.server.reviral.config.response.exception.BasicException
import tech.server.reviral.config.response.exception.enums.BasicError
import tech.server.reviral.config.security.JwtRedisRepository
import tech.server.reviral.config.security.JwtToken
import tech.server.reviral.config.security.JwtTokenProvider
import java.time.LocalDateTime

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
    private val userInfoRepository: UserInfoRepository,
    private val pointRepository: PointRepository
) {

    @Transactional
    @Throws(BasicException::class)
    fun loadUserByUsername( userId: Long ): User {
        return accountRepository.findById(userId)
            .orElseThrow{ throw BasicException(BasicError.USER_NOT_EXIST) }
    }

    @Transactional
    @Throws(BasicException::class)
    fun signup(request: OAuthUserInfo, registration: Registration, accessToken: String, refreshToken: String): JwtToken {

        val isExistUser = accountRepository.existsBySidAndRegistration(request.getSid(), registration)

        if ( !isExistUser ) {
            val newUser = this.accountRepository.save(
                User(
                    email = request.getEmail(),
                    registration = request.getProvider(),
                    profileImage = request.getThumbnailImage(),
                    sid = request.getSid(),
                    auth = UserRole.ROLE_REVIEWER,
                    userPassword = passwordEncoder.encode(accessToken),
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            )

            this.pointRepository.save(
                Point(
                    user = newUser,
                    remainPoint = 0,
                    expectPoint = 0,
                    totalChangePoint = 0,
                    createAt = LocalDateTime.now()
                )
            )

            val authenticationToken = UsernamePasswordAuthenticationToken( newUser.id, accessToken )

            val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

            val account = authentication.principal as User

            return jwtTokenProvider.getJwtToken(account)

        }else {
            val user = accountRepository.findBySidAndRegistration(request.getSid(), registration)

            user.userPassword = passwordEncoder.encode(accessToken)
            user.profileImage = request.getThumbnailImage()
            user.email = request.getEmail()
            user.accessToken = accessToken
            user.refreshToken = refreshToken
            user.updatedAt = LocalDateTime.now()

            val authenticationToken = UsernamePasswordAuthenticationToken( user.id, accessToken )

            val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

            val account = authentication.principal as User

            return jwtTokenProvider.getJwtToken(account)
        }
    }

    @Transactional
    @Throws(BasicException::class)
    fun setBasicUserInfo( request: BasicUserInfoRequestDTO ): Boolean {

        val user = accountRepository.findById(request.userId)
            .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

        user.isEvent = request.isEvent
        accountRepository.save(user)

        val userInfo = if ( userInfoRepository.existsByUser(user) ) {
            this.userInfoRepository.findByUser(user)
        }else {
            this.userInfoRepository.save(UserInfo(
                user = user,
                secondPassword = passwordEncoder.encode(request.password),
                username = request.name,
                gender = request.gender,
                phone = request.phoneNumber.replace("-","").trim(),
            ))
        }

        user.userInfo = userInfo
        this.accountRepository.save(user)

        return true
    }

    /**
     * 사용자 설정 정보 조회
     * @param userId: String
     * @return UserInfoResponseDTO
     * @exception BasicException
     */
    @Transactional
    @Throws(BasicException::class)
    fun getUserInfo(userId: Long): UserInfoResponseDTO {

        val user = accountRepository.findById(userId)
            .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

        return UserInfoResponseDTO(
            name = user.userInfo?.username,
            loginId = user.email!!,
            nvId = user.userInfo?.nvId,
            cpId = user.userInfo?.cpId,
            phoneNumber = user.userInfo?.phone,
            address = user.userInfo?.address,
            bankCode = user.userInfo?.bankCode,
            accountNumber = user?.userInfo?.account,
            profileImage = user.profileImage ?: ""
        )
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
     * 사용자 개인정보 업데이트 서비스
     * @param request: UpdateUserInfoRequestDTO
     * @return Boolean 업데이트 유무
     * @exception BasicException
     */
    @Transactional
    @Throws(BasicException::class)
    fun setUserInfo(request: UpdateUserInfoRequestDTO, userId: Long): Boolean {

        val user = accountRepository.findById(userId)
            .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

        request.phoneNumber?.let { user?.userInfo?.phone = it }
        request.address?.let { user?.userInfo?.address = it }
        request.bankCode?.let { user?.userInfo?.bankCode = it }
        request.accountNumber?.let { user?.userInfo?.account = it }
        request.nvId?.let { user?.userInfo?.nvId = it }
        request.cpId?.let { user?.userInfo?.cpId = it }
        request.password?.let { user?.userInfo?.secondPassword = passwordEncoder.encode(it) }
        user?.userInfo?.updatedAt = LocalDateTime.now()

        accountRepository.save(user)

        return true
    }

    /**
     * 2차 비밀번호 검증 서비스
     * @param request: ValidationPasswordRequestDTO
     * @return 비밀번호 검증여부
     * @exception BasicException
     */
    @Transactional
    @Throws(BasicException::class)
    fun isValidPassword(request: ValidPasswordRequestDTO): Boolean {

        val user = accountRepository.findById(request.userId)
            .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

        val secondPassword = user.userInfo?.secondPassword
            ?: throw BasicException(BasicError.SECONDARY_PASSWORD_SET)

        return passwordEncoder.matches(request.password, secondPassword)
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

        if (userId == claim.sub) { // 재발급 토큰 비교값이 동일할 경우
            val token = jwtTokenProvider.getJwtToken(loadUserByUsername(userId.toLong()))
            jwtRedisRepository.delete(request.refreshToken)
            return token
        }else {
            throw BasicException(BasicError.TOKEN_NOT_MATCH)
        }
    }

    @Transactional
    @Throws(BasicException::class)
    fun hasUserInfo(request: HttpServletRequest): Boolean {
        val hasToken = jwtTokenProvider.extractToken(request)
        if ( hasToken.isEmpty ) {
            throw BasicException(BasicError.UNAUTHORIZED)
        }else {
            val hasClaims = jwtTokenProvider.decryptClaims(hasToken.get())

            val userId = hasClaims.get().subject.toLong()

            val user = accountRepository.findById(userId)
                .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

            return userInfoRepository.existsByUser(user)
        }
    }

}
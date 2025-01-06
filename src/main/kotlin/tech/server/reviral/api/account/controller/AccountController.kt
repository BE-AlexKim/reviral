package tech.server.reviral.api.account.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.server.reviral.api.account.model.dto.*
import tech.server.reviral.api.account.service.AccountService
import tech.server.reviral.common.config.docs.account.*
import tech.server.reviral.common.config.response.success.WrapResponseEntity
import tech.server.reviral.common.config.security.JwtToken

/**
 *packageName    : tech.server.reviral.api.account.controller
 * fileName       : AccountController
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "회원 정보")
class AccountController constructor(
    private val accountService: AccountService
){

    @PostMapping("/sign-in")
    @SignInExplain
    fun login( @RequestBody request: SignInRequestDTO ): ResponseEntity<WrapResponseEntity<JwtToken>> {
        val token = accountService.signIn(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.ACCEPTED, "jwt", token)
    }

    @PostMapping("/sign-up")
    @SignUpExplain
    fun signUp(@RequestBody @Valid request: SignUpRequestDTO): ResponseEntity<WrapResponseEntity<Boolean>> {
        val signUp = accountService.signUp(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "signUp", signUp)
    }

    @GetMapping("/id-check")
    @IdCheckExplain
    fun isLoginIdDuplicated(@RequestParam loginId: String): ResponseEntity<WrapResponseEntity<Boolean>> {
        val isLoginIdDuplicated = accountService.isLoginIdDuplicated(loginId)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isDuplicated", isLoginIdDuplicated)
    }

    @PostMapping("/reload")
    @JwtReloadExplain
    fun reloadUserByRefreshToken(@RequestBody request: ReloadRequestDTO): ResponseEntity<WrapResponseEntity<JwtToken>> {
        val reissueToken =  accountService.reloadUserByRefreshToken(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "jwt",reissueToken)
    }

    @PostMapping("/logout/{userId}")
    @LogoutExplain
    fun logout(@PathVariable userId: Long) {
        accountService.logout(userId)
    }

    @PostMapping("/info/{username}")
    @UserInfoExplain
    fun getUserInfo(@PathVariable username: String): ResponseEntity<WrapResponseEntity<UserInfoResponseDTO>> {
        val userInfo = accountService.getUserInfo(username)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "userInfo", userInfo)
    }

    @PostMapping("/email/authorized")
    @SendAuthorizedEmailExplain
    fun sendAuthorizedCode(@RequestBody request: EmailAuthorizedRequestDTO): ResponseEntity<WrapResponseEntity<Boolean>> {
        val send = accountService.sendAuthorizedToEmail(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isSend", send)
    }

    @PostMapping("/email/verify")
    @VerifyEmailAuthorizedCodeExplain
    fun verifyAuthorizedCode( @RequestBody request: AuthorizeCodeRequestDTO ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val isVerifyCode = accountService.verifyAuthorizedEmailCode(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isVerify", isVerifyCode)
    }

}
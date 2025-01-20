package tech.server.reviral.api.account.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
    fun signIn( @RequestBody request: SignInRequestDTO ): ResponseEntity<WrapResponseEntity<JwtToken>> {
        val token = accountService.signIn(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.ACCEPTED, "jwt", token)
    }

    @PostMapping("/admin/sign-in")
    fun login(@RequestBody request: SignInRequestDTO): ResponseEntity<WrapResponseEntity<JwtToken>> {
        val login = accountService.signInToAdmin(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK,"jwt", login)
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

    @PostMapping("/info/{userId}")
    @UserInfoExplain
    fun getUserInfo(@PathVariable userId: Long): ResponseEntity<WrapResponseEntity<UserInfoResponseDTO>> {
        val userInfo = accountService.getUserInfo(userId)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "userInfo", userInfo)
    }

    @PostMapping("/email/authorized/{type}")
    @SendAuthorizedEmailExplain
    fun sendAuthorizedCode(
        @PathVariable("type") type: String,
        @RequestBody request: EmailAuthorizedRequestDTO
    ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val send = accountService.sendAuthorizedToEmail(type,request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isSend", send)
    }

    @PostMapping("/email/verify/{type}")
    @VerifyEmailAuthorizedCodeExplain
    fun verifyAuthorizedCode(
        @PathVariable("type") type:String,
        @RequestBody request: AuthorizeCodeRequestDTO
    ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val isVerifyCode = accountService.verifyAuthorizedEmailCode(type,request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isVerify", isVerifyCode)
    }

    @PutMapping("/update")
    @UpdateUserInfoExplain
    fun updateUserInfo( @RequestBody request: UpdateUserInfoRequestDTO ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val update = accountService.updateUserInfo(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isUpdated", update)
    }

    @PostMapping("/verify/{type}")
    @ValidationPasswordExplain
    fun isValidationPassword(
        @RequestBody request: ValidationPasswordRequestDTO,
        @PathVariable("type") type: String
    ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val isValidation = accountService.isValidPassword(request, type)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK,"isValid", isValidation)
    }

}
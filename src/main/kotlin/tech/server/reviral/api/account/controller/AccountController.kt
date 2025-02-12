package tech.server.reviral.api.account.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.server.reviral.api.account.model.dto.*
import tech.server.reviral.api.account.service.AccountService
import tech.server.reviral.common.config.docs.account.*
import tech.server.reviral.common.config.message.MessageService
import tech.server.reviral.common.config.response.exception.BasicException
import tech.server.reviral.common.config.response.exception.enums.BasicError
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
    private val accountService: AccountService,
    private val messageService: MessageService
){

    @PhoneAuthorizeExplain
    @PostMapping("/phone/authorize/{type}")
    fun getPhoneAuthorize(
        @RequestBody request: PhoneAuthorizeRequestDTO,
        @PathVariable type: String
    ): ResponseEntity<WrapResponseEntity<Boolean>> {
        return when(type) {
            "valid" -> {
                val isValid = messageService.isValidAuthCode(request.phone, request.code!!)
                WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isValid", isValid)
            }
            "send" -> {
                val send = messageService.sendAuthorizationCode(request.phone)
                WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isSend", send)
            }
            else -> {
                throw BasicException(BasicError.DEFAULT)
            }
        }
    }

    @BasicUserInfoExplain
    @PostMapping("/info/basic")
    fun setStandardUserInfo(@RequestBody request: BasicUserInfoRequestDTO): ResponseEntity<WrapResponseEntity<Boolean>> {
        val basic = accountService.setBasicUserInfo(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isSave", true)
    }

    @UserInfoExplain
    @PostMapping("/info/{userId}")
    fun getUserInfo(@PathVariable userId: Long): ResponseEntity<WrapResponseEntity<UserInfoResponseDTO>> {
        val userInfo = accountService.getUserInfo(userId)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "userInfo", userInfo)
    }

    @UpdateUserInfoExplain
    @PutMapping("/info/{userId}")
    fun setUserInfo(
        @PathVariable userId: Long,
        @RequestBody request: UpdateUserInfoRequestDTO
    ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val update = accountService.setUserInfo(request, userId)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isUpdated", update)
    }

    @GetMapping("/info/verify")
    fun hasUserInfo(
        request: HttpServletRequest
    ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val hasUserInfo = accountService.hasUserInfo(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "hasUserInfo", hasUserInfo)
    }

    @JwtReloadExplain
    @PostMapping("/reload")
    fun reloadUserByRefreshToken(@RequestBody request: ReloadRequestDTO): ResponseEntity<WrapResponseEntity<JwtToken>> {
        val reissueToken =  accountService.reloadUserByRefreshToken(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "jwt",reissueToken)
    }

    @LogoutExplain
    @PostMapping("/logout/{userId}")
    fun logout(@PathVariable userId: Long) {
        accountService.logout(userId)
    }

    @PostMapping("/verify/password")
    @ValidationPasswordExplain
    fun isValidationPassword(
        @RequestBody request: ValidPasswordRequestDTO
    ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val isValidation = accountService.isValidPassword(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK,"isValid", isValidation)
    }
}
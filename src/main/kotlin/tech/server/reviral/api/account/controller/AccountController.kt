package tech.server.reviral.api.account.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.server.reviral.api.account.model.dto.SignInRequestDTO
import tech.server.reviral.api.account.model.dto.SignUpRequestDTO
import tech.server.reviral.api.account.service.AccountService
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
    fun login( @RequestBody request: SignInRequestDTO ): ResponseEntity<WrapResponseEntity<JwtToken>> {
        val token = accountService.getJwtToken(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.ACCEPTED, "jwt", token)
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequestDTO): ResponseEntity<WrapResponseEntity<Boolean>> {
        val signUp = accountService.signUp(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "signUp", signUp)
    }

}
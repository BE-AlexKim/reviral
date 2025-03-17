package tech.server.reviral.api.bussiness.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.server.reviral.api.account.model.dto.SignInRequestDTO
import tech.server.reviral.api.bussiness.model.dto.AdminSignupRequestDTO
import tech.server.reviral.api.bussiness.model.dto.ReviewerInfoResponseDTO
import tech.server.reviral.api.bussiness.service.SellerAccountService
import tech.server.reviral.config.docs.bussiness.ReviewerInfoExplain
import tech.server.reviral.config.response.success.WrapResponseEntity
import tech.server.reviral.config.security.JwtToken

/**
 *packageName    : tech.server.reviral.api.bussiness.controller
 * fileName       : SellerAccountController
 * author         : joy58
 * date           : 2025-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-13        joy58       최초 생성
 */
@Tag(name = "어드민 계정 API")
@RestController
@RequestMapping("/api/v1/business")
class SellerAccountController constructor(
    private val sellerAccountService: SellerAccountService
) {

    @ReviewerInfoExplain
    @GetMapping("/reviewers")
    fun getReviewers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<WrapResponseEntity<Page<ReviewerInfoResponseDTO>>> {
        val pageable = PageRequest.of(page,size)
        val reviewers = sellerAccountService.getReviewers(pageable)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK,"reviewers", reviewers)
    }

    @PostMapping("/signup")
    fun signup(@RequestBody request: AdminSignupRequestDTO): ResponseEntity<WrapResponseEntity<Boolean>> {
        val signup = sellerAccountService.signup(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "signup", signup)
    }

    @PostMapping("/sign-in")
    fun login(@RequestBody request: SignInRequestDTO): ResponseEntity<WrapResponseEntity<JwtToken>> {
        val login = sellerAccountService.login(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "jwt", login)
    }

}
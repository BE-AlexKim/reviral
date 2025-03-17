package tech.server.reviral.api.oauth.controller

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.server.reviral.api.account.model.enums.Registration
import tech.server.reviral.api.oauth.model.dto.ExpireUser
import tech.server.reviral.api.oauth.service.OAuthService
import tech.server.reviral.config.docs.oauth.OAuthSignupExplain
import tech.server.reviral.config.docs.oauth.OAuthUnlinkExplain
import tech.server.reviral.config.response.success.WrapResponseEntity
import tech.server.reviral.config.security.JwtToken
import tech.server.reviral.config.security.JwtTokenProvider

/**
 *packageName    : tech.server.reviral.common.config.oauth
 * fileName       : OAuthController
 * author         : joy58
 * date           : 2025-02-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-07        joy58       최초 생성
 */
@Tag(name = "OAuth 2.0 API")
@RestController
@RequestMapping("/api/v1/oauth")
class OAuthController constructor(
    private val oAuthService: OAuthService,
    private val jwtTokenProvider: JwtTokenProvider
){

    @GetMapping("/authorize/{provider}")
    fun getAuthorizationUri(
        @PathVariable provider: String,
        @RequestParam("redirect_uri") redirectUri: String?
    ): ResponseEntity<WrapResponseEntity<String>>{
        val uri = oAuthService.getAuthorizationUri(Registration.valueOf(provider.uppercase()), redirectUri)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "uri", uri)
    }

    @OAuthSignupExplain
    @PostMapping("/callback/{provider}")
    fun getAuthorizationCode(
        @RequestParam code: String,
        @PathVariable provider: String
    ): ResponseEntity<WrapResponseEntity<JwtToken>> {
        val oAuthToken = oAuthService.getAccessToken(code, provider)
        val jwtToken = oAuthService.getUserInfo(oAuthToken, provider)
        val claims = jwtTokenProvider.decryptClaims(jwtToken.accessToken)
        val hasUserInfo = claims.get()["hasUserInfo"] as Boolean
        jwtToken.hasUserInfo = hasUserInfo
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "jwt", jwtToken)
    }

    @OAuthUnlinkExplain
    @PostMapping("/unlink")
    fun expireUser( @RequestBody request: ExpireUser ): ResponseEntity<WrapResponseEntity<Boolean>> {
        val expire = oAuthService.unlink(request.userId)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK,"isExpire", expire)
    }

}
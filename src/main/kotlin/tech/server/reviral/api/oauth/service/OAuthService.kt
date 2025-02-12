package tech.server.reviral.api.oauth.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.account.model.enums.Registration
import tech.server.reviral.api.account.repository.AccountRepository
import tech.server.reviral.api.account.service.AccountService
import tech.server.reviral.api.oauth.config.OAuthProperties
import tech.server.reviral.api.oauth.model.OAuthToken
import tech.server.reviral.api.oauth.model.dto.*
import tech.server.reviral.common.config.response.exception.BasicException
import tech.server.reviral.common.config.response.exception.enums.BasicError
import tech.server.reviral.common.config.security.JwtToken
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.common.config.oauth
 * fileName       : OAuthService
 * author         : joy58
 * date           : 2025-02-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-07        joy58       최초 생성
 */
@Service
class OAuthService constructor(
    private val oAuthProperties: OAuthProperties,
    private val accountService: AccountService,
    private val accountRepository: AccountRepository
) {

    private val log = KotlinLogging.logger{}
    private val restTemplate = RestTemplate()

    @Transactional
    @Throws(BasicException::class)
    fun getAuthorizationUri(registration: Registration, redirectUri: String?): String {

        return when ( registration ) {
            Registration.KAKAO -> {
                UriComponentsBuilder.fromHttpUrl(oAuthProperties.provider.kakao.authorizationUri)
                    .queryParam("client_id",oAuthProperties.registration.kakao.clientId)
                    .queryParam("scope", oAuthProperties.registration.kakao.scope.joinToString(" "))
                    .queryParam("redirect_uri",redirectUri ?: oAuthProperties.registration.kakao.redirectUri)
                    .queryParam("response_type",oAuthProperties.registration.kakao.responseType)
                    .queryParam("prompt","login")
                    .toUriString()
            }

            Registration.NAVER -> {
                UriComponentsBuilder.fromHttpUrl(oAuthProperties.provider.naver.authorizationUri)
                    .queryParam("client_id",oAuthProperties.registration.naver.clientId)
                    .queryParam("scope",oAuthProperties.registration.naver.scope.joinToString(" "))
                    .queryParam("redirect_uri",redirectUri ?: oAuthProperties.registration.naver.redirectUri)
                    .queryParam("response_type",oAuthProperties.registration.naver.responseType)
                    .toUriString()
            }

            else -> throw BasicException(BasicError.UNSUPPORTED_REGISTRATION)
        }
    }

    @Transactional
    @Throws(BasicException::class)
    fun getAccessToken(code: String, provider: String): OAuthToken? {
        return when (Registration.valueOf(provider.uppercase())) {
            Registration.KAKAO -> getKakaoAccessToken(code)
            Registration.NAVER -> getNaverAccessToken(code)
            else -> throw BasicException(BasicError.UNSUPPORTED_REGISTRATION)
        }
    }

    @Throws(BasicException::class)
    private fun getKakaoAccessToken(code: String): OAuthToken? {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.set("Accept", "application/json")

        val formData = LinkedMultiValueMap<String, String>()
        formData.set("grant_type", oAuthProperties.registration.kakao.authorizationGrantType)
        formData.set("client_id", oAuthProperties.registration.kakao.clientId)
        formData.set("redirect_uri", oAuthProperties.registration.kakao.redirectUri)
        formData.set("code", code)
        formData.set("client_secret", oAuthProperties.registration.kakao.clientSecret)

        val request = HttpEntity(formData, headers)

        try {
            return restTemplate.postForEntity(oAuthProperties.provider.kakao.tokenUri, request, KakaoToken::class.java).body
        }catch (ex: HttpClientErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.OAUTH_CLIENT_ERROR)
        }catch (ex: HttpServerErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.DEFAULT)
        }catch (ex: ResourceAccessException) {
            log.error { ex.message }
            throw BasicException(BasicError.SERVER_NETWORK_ERROR)
        }catch (ex: HttpMessageNotReadableException) {
            log.error { ex.message }
            throw BasicException(BasicError.RESPONSE_TYPE_ERROR)
        }
    }

    @Throws(BasicException::class)
    private fun getNaverAccessToken(code: String): OAuthToken? {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.set("Accept", "application/json")

        val formData = LinkedMultiValueMap<String, String>()
        formData.set("grant_type", oAuthProperties.registration.naver.authorizationGrantType)
        formData.set("client_id", oAuthProperties.registration.naver.clientId)
        formData.set("redirect_uri", oAuthProperties.registration.naver.redirectUri)
        formData.set("code", code)
        formData.set("client_secret", oAuthProperties.registration.naver.clientSecret)

        val request = HttpEntity(formData, headers)

        try {
            return restTemplate.postForEntity(oAuthProperties.provider.naver.tokenUri, request, NaverToken::class.java).body
        }catch (ex: HttpClientErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.OAUTH_CLIENT_ERROR)
        }catch (ex: HttpServerErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.DEFAULT)
        }catch (ex: ResourceAccessException) {
            log.error { ex.message }
            throw BasicException(BasicError.SERVER_NETWORK_ERROR)
        }catch (ex: HttpMessageNotReadableException) {
            log.error { ex.message }
            throw BasicException(BasicError.RESPONSE_TYPE_ERROR)
        }
    }

    @Transactional
    @Throws(BasicException::class)
    fun getUserInfo(oAuthToken: OAuthToken?, provider: String): JwtToken {
        return when (Registration.valueOf(provider.uppercase())) {
            Registration.KAKAO -> getKakaoUserInfo(oAuthToken)
            Registration.NAVER -> getNaverUserInfo(oAuthToken)
            else -> throw BasicException(BasicError.UNSUPPORTED_REGISTRATION)
        }
    }

    @Throws(BasicException::class)
    private fun getKakaoUserInfo(oAuthToken: OAuthToken?): JwtToken {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("Authorization", "${oAuthToken?.getTokenType()} ${oAuthToken?.getAccessToken()}")

        val requestEntity = HttpEntity(null, headers)

        try {

            val responseEntity = restTemplate
                .exchange(
                    oAuthProperties.provider.kakao.userInfoUri,
                    HttpMethod.GET,
                    requestEntity,
                    KakaoUserInfo::class.java
                ).body!!

            return accountService.signup(responseEntity, Registration.KAKAO,oAuthToken?.getAccessToken()!!, oAuthToken.getRefreshToken()!!)

        }catch (ex: HttpClientErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.OAUTH_CLIENT_ERROR)
        }catch (ex: HttpServerErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.DEFAULT)
        }catch (ex: ResourceAccessException) {
            log.error { ex.message }
            throw BasicException(BasicError.SERVER_NETWORK_ERROR)
        }catch (ex: HttpMessageNotReadableException) {
            log.error { ex.message }
            throw BasicException(BasicError.RESPONSE_TYPE_ERROR)
        }
    }

    @Throws(BasicException::class)
    private fun getNaverUserInfo(oAuthToken: OAuthToken?): JwtToken {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("Authorization", "${oAuthToken?.getTokenType()} ${oAuthToken?.getAccessToken()}")

        val requestEntity = HttpEntity(null, headers)

        try {
            val responseEntity = restTemplate
                .postForEntity(
                    oAuthProperties.provider.naver.userInfoUri,
                    requestEntity,
                    NaverUserInfo::class.java
                ).body!!

            return accountService.signup(responseEntity, Registration.NAVER, oAuthToken?.getAccessToken()!!, oAuthToken.getRefreshToken()!!)

        }catch (ex: HttpClientErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.OAUTH_CLIENT_ERROR)
        }catch (ex: HttpServerErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.DEFAULT)
        }catch (ex: ResourceAccessException) {
            log.error { ex.message }
            throw BasicException(BasicError.SERVER_NETWORK_ERROR)
        }catch (ex: HttpMessageNotReadableException) {
            log.error { ex.message }
            throw BasicException(BasicError.RESPONSE_TYPE_ERROR)
        }
    }

    @Transactional
    @Throws(BasicException::class)
    fun unlink(userId: Long): Boolean {
        val user = accountRepository.findById(userId)
            .orElseThrow { throw BasicException(BasicError.USER_NOT_EXIST) }

        return when ( user.registration ) {
            Registration.KAKAO -> unlinkToKakao(user)
            Registration.NAVER -> unlinkToNaver(user)
            else -> throw BasicException(BasicError.UNSUPPORTED_REGISTRATION)
        }
    }

    @Transactional
    @Throws(BasicException::class)
    fun unlinkToKakao(user: User): Boolean {

        val headers = HttpHeaders()
        headers.set("Content-Type","application/x-www-form-urlencoded;charset=utf-8")
        headers.set("Authorization", "KakaoAK ${oAuthProperties.registration.kakao.adminKey}")

        val formData = LinkedMultiValueMap<String,Any>()
        formData.add("target_id_type","user_id")
        formData.add("target_id",user.sid?.toLong())

        val requestEntity = HttpEntity(formData, headers)

        try {
            restTemplate.postForEntity(
                    oAuthProperties.provider.kakao.unlinkUri,
                    requestEntity,
                    KakaoUnlink::class.java
                ).body!!

            log.info { "USER_ID : ${user.id}, PROVIDER:KAKAO, MESSAGE: UNLINK" }

            user.isUserNonLocked = false
            user.isEvent = false
            user.updatedAt = LocalDateTime.now()

            this.accountRepository.save(user)

            return true
        }catch (ex: HttpClientErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.OAUTH_CLIENT_ERROR)
        }catch (ex: HttpServerErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.DEFAULT)
        }catch (ex: ResourceAccessException) {
            log.error { ex.message }
            throw BasicException(BasicError.SERVER_NETWORK_ERROR)
        }catch (ex: HttpMessageNotReadableException) {
            log.error { ex.message }
            throw BasicException(BasicError.RESPONSE_TYPE_ERROR)
        }
    }

    @Transactional
    @Throws(BasicException::class)
    fun unlinkToNaver(user: User): Boolean {

        val token = reissueNaverToken(user)

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.setBearerAuth(token?.naverAccessToken!!)

        val formData = LinkedMultiValueMap<String,String>()
        formData.add("client_id",oAuthProperties.registration.naver.clientId)
        formData.add("client_secret",oAuthProperties.registration.naver.clientSecret)
        formData.add("access_token",token.naverAccessToken)
        formData.add("service_provider","NAVER")
        formData.add("grant_type","delete")

        val requestEntity = HttpEntity(formData, null)

        try {
            restTemplate.postForEntity(
                    oAuthProperties.provider.kakao.unlinkUri,
                    requestEntity,
                    KakaoUnlink::class.java
                ).body!!

            log.info { "USER_ID : ${user.id}, PROVIDER:KAKAO, MESSAGE: UNLINK" }

            return true
        }catch (ex: HttpClientErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.OAUTH_CLIENT_ERROR)
        }catch (ex: HttpServerErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.DEFAULT)
        }catch (ex: ResourceAccessException) {
            log.error { ex.message }
            throw BasicException(BasicError.SERVER_NETWORK_ERROR)
        }catch (ex: HttpMessageNotReadableException) {
            log.error { ex.message }
            throw BasicException(BasicError.RESPONSE_TYPE_ERROR)
        }
    }

    @Transactional
    @Throws(BasicException::class)
    fun reissueNaverToken(user: User): NaverToken? {

        val formData = LinkedMultiValueMap<String,String>()
        formData.add("client_id", oAuthProperties.registration.naver.clientId)
        formData.add("client_secret", oAuthProperties.registration.naver.clientSecret)
        formData.add("refresh_token", user.refreshToken)
        formData.add("grant_type","refresh_token")

        val requestEntity = HttpEntity(formData,null)

        try {
            return restTemplate.postForEntity(
                oAuthProperties.provider.naver.tokenUri,
                requestEntity,
                NaverToken::class.java
            ).body
        }catch (ex: HttpClientErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.OAUTH_CLIENT_ERROR)
        }catch (ex: HttpServerErrorException) {
            log.error { ex.message }
            throw BasicException(BasicError.DEFAULT)
        }catch (ex: ResourceAccessException) {
            log.error { ex.message }
            throw BasicException(BasicError.SERVER_NETWORK_ERROR)
        }catch (ex: HttpMessageNotReadableException) {
            log.error { ex.message }
            throw BasicException(BasicError.RESPONSE_TYPE_ERROR)
        }
    }
}
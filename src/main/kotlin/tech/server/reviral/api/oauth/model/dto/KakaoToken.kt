package tech.server.reviral.api.oauth.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.oauth.model.OAuthToken

/**
 *packageName    : tech.server.reviral.common.config.oauth
 * fileName       : KakaoToken
 * author         : joy58
 * date           : 2025-02-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-07        joy58       최초 생성
 */
@Schema(name = "카카오 토큰 발급 응답 모델")
data class KakaoToken(
    @JsonProperty("access_token")
    val kakaoAccessToken: String,

    @JsonProperty("token_type")
    val kakaoTokenType: String,

    @JsonProperty("refresh_token")
    val kakaoRefreshToken: String?,

    @JsonProperty("expires_in")
    val kakaoExpiresIn: Int,

    @JsonProperty("scope")
    val kakaoScope: String,

    @JsonProperty("refresh_token_expires_in")
    val kakaoRefreshTokenExpiresIn: Int?

): OAuthToken {

    override fun getAccessToken(): String {
        return kakaoAccessToken
    }

    override fun getRefreshToken(): String? {
        return kakaoRefreshToken
    }

    override fun getTokenType(): String {
        return kakaoTokenType
    }

    override fun getExpiresIn(): Int {
        return kakaoExpiresIn
    }
}

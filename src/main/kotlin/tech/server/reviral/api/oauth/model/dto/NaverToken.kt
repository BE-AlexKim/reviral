package tech.server.reviral.api.oauth.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import tech.server.reviral.api.oauth.model.OAuthToken

/**
 *packageName    : tech.server.reviral.common.config.oauth
 * fileName       : NaverToken
 * author         : joy58
 * date           : 2025-02-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-07        joy58       최초 생성
 */
@Schema(name = "네이버 토큰 발급 응답 모델")
data class NaverToken(
    @JsonProperty("access_token")
    val naverAccessToken: String,

    @JsonProperty("refresh_token")
    val naverRefreshToken: String,

    @JsonProperty("token_type")
    val naverTokenType: String,

    @JsonProperty("expires_in")
    val naverExpiresIn: Int,

): OAuthToken {

    override fun getAccessToken(): String {
        return naverAccessToken
    }

    override fun getRefreshToken(): String {
        return naverRefreshToken
    }

    override fun getTokenType(): String {
        return naverTokenType
    }

    override fun getExpiresIn(): Int {
        return naverExpiresIn
    }
}

package tech.server.reviral.api.oauth.model

/**
 *packageName    : tech.server.reviral.common.config.oauth
 * fileName       : OAuthTokens
 * author         : joy58
 * date           : 2025-02-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-07        joy58       최초 생성
 */
interface OAuthToken {

    fun getAccessToken(): String

    fun getRefreshToken(): String?

    fun getTokenType(): String

    fun getExpiresIn(): Int
}
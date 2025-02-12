package tech.server.reviral.api.oauth.model

import tech.server.reviral.api.account.model.enums.Registration

/**
 *packageName    : tech.server.reviral.common.config.oauth.model
 * fileName       : OAuthUserInfo
 * author         : joy58
 * date           : 2025-02-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-08        joy58       최초 생성
 */
interface OAuthUserInfo {

    fun getUsername(): String?

    fun getSid(): String

    fun getEmail(): String?

    fun getProvider(): Registration

    fun getThumbnailImage(): String?
}
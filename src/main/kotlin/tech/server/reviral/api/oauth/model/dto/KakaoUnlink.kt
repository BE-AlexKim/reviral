package tech.server.reviral.api.oauth.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *packageName    : tech.server.reviral.common.config.oauth.model.dto
 * fileName       : KakaoUnlink
 * author         : joy58
 * date           : 2025-02-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-08        joy58       최초 생성
 */
data class KakaoUnlink(
    @JsonProperty("id")
    val sid: Long
)

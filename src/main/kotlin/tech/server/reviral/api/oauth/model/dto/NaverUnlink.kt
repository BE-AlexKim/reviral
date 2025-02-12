package tech.server.reviral.api.oauth.model.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *packageName    : tech.server.reviral.api.oauth.model.dto
 * fileName       : NaverUnlink
 * author         : joy58
 * date           : 2025-02-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-08        joy58       최초 생성
 */
data class NaverUnlink(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("result")
    val result: String
)

package tech.server.reviral.api.oauth.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import tech.server.reviral.api.account.model.enums.Registration
import tech.server.reviral.api.oauth.model.OAuthUserInfo

/**
 *packageName    : tech.server.reviral.api.account.model.dto
 * fileName       : NaverUserInfoDTO
 * author         : joy58
 * date           : 2025-02-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-06        joy58       최초 생성
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class NaverUserInfo(
    @JsonProperty("resultcode")
    val resultCode: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("response")
    val response: NaverAccount
): OAuthUserInfo {

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class NaverAccount(
        @JsonProperty("id")
        val id: String, // 네이버 고유 ID는 필수 값이므로 nullable 처리하지 않음

        @JsonProperty("email")
        val email: String?, // 이메일은 선택적으로 제공될 수 있음

        @JsonProperty("nickname")
        val nickname: String?, // 닉네임이 없을 수도 있음

        @JsonProperty("profile_image")
        val profileImage: String?, // 프로필 이미지 URL

        @JsonProperty("age")
        val age: String?, // 연령대 (예: "20-29")

        @JsonProperty("gender")
        val gender: String?, // 성별 (M: 남성, F: 여성)

        @JsonProperty("name")
        val name: String?, // 사용자의 실명 (선택적 제공)

        @JsonProperty("birthday")
        val birthday: String?, // 생일 (MM-DD 형식)

        @JsonProperty("birthyear")
        val birthYear: String?, // 출생 연도 (예: "1990")

        @JsonProperty("mobile")
        val mobile: String?, // 휴대전화 번호 (하이픈 포함)

        @JsonProperty("mobile_e164")
        val mobileE164: String? // 국제 표준 형식의 전화번호 (E.164)
    )

    override fun getUsername(): String? {
        return response.name ?: response.nickname
    }

    override fun getSid(): String {
        return response.id
    }

    override fun getEmail(): String? {
        return response.email
    }

    override fun getProvider(): Registration {
        return Registration.NAVER
    }

    override fun getThumbnailImage(): String? {
        return response.profileImage
    }
}

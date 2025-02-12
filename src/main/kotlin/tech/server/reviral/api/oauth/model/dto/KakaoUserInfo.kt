package tech.server.reviral.api.oauth.model.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import tech.server.reviral.api.account.model.enums.Registration
import tech.server.reviral.api.oauth.model.OAuthUserInfo

/**
 *packageName    : tech.server.reviral.common.config.oauth
 * fileName       : KakaoUserInfo
 * author         : joy58
 * date           : 2025-02-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-07        joy58       최초 생성
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoUserInfo(
    @JsonProperty("id")
    val id: Long?,

    @JsonProperty("connected_at")
    val connectedAt: String?,

    @JsonProperty("synched_at")
    val synchedAt: String?,

    @JsonProperty("properties")
    val properties: UserProperties,

    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoAccount
): OAuthUserInfo {

    override fun getUsername(): String? {
        return kakaoAccount.name ?: properties.nickname ?: kakaoAccount.profile.nickname
    }

    override fun getSid(): String {
        return id.toString()
    }

    override fun getEmail(): String? {
        return kakaoAccount.email
    }

    override fun getProvider(): Registration {
        return Registration.KAKAO
    }

    override fun getThumbnailImage(): String? {
        return properties.profileImage ?: properties.thumbnailImage
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class UserProperties(
        @JsonProperty("nickname")
        val nickname: String?,

        @JsonProperty("profile_image")
        val profileImage: String?,

        @JsonProperty("thumbnail_image")
        val thumbnailImage: String?,
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class KakaoAccount(
        @JsonProperty("profile_nickname_needs_agreement")
        val profileNicknameNeedsAgreement: Boolean?,

        @JsonProperty("profile")
        val profile: KakaoProfile,

        @JsonProperty("name_needs_agreement")
        val nameNeedsAgreement: Boolean?,

        @JsonProperty("name")
        val name: String?,

        @JsonProperty("has_email")
        val hasEmail: Boolean?,

        @JsonProperty("email_needs_agreement")
        val emailNeedsAgreement: Boolean?,

        @JsonProperty("is_email_valid")
        val isEmailValid: Boolean?,

        @JsonProperty("is_email_verified")
        val isEmailVerified: Boolean?,

        @JsonProperty("email")
        val email: String?,

        @JsonProperty("has_phone_number")
        val hasPhoneNumber: Boolean?,

        @JsonProperty("phone_number")
        val phoneNumber: String?,

        @JsonProperty("phone_number_needs_agreement")
        val phoneNumberNeedsAgreement: Boolean?,

        @JsonProperty("has_gender")
        val hasGender: Boolean?,

        @JsonProperty("gender")
        val gender: String?,

        @JsonProperty("gender_needs_agreement")
        val genderNeedsAgreement: Boolean?
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class KakaoProfile(
        @JsonProperty("nickname")
        val nickname: String?,

        @JsonProperty("is_default_nickname")
        val isDefaultNickname: Boolean?
    )
}

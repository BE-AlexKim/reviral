package tech.server.reviral.api.account.model.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import tech.server.reviral.api.account.model.enums.Gender
import tech.server.reviral.api.account.model.enums.Registration
import tech.server.reviral.api.account.model.enums.UserRole
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.account.model.entity
 * fileName       : User
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@Entity
@Table(name = "tb_user_info")
@Comment("사용자 정보 테이블")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Comment("사용자 일련번호")
    val id: Long? = null,

    @Column(name = "provider", nullable = false, length = 10)
    @Comment("소셜 가입 제공자")
    @Enumerated(EnumType.STRING)
    val registration: Registration,

    @Column(name = "user_sid")
    @Comment("소셜 일련번호")
    val sid: String? = null,

    @Column(name = "user_password")
    @Comment("사용자 비밀번호")
    var userPassword: String? = null,

    @Column(name = "email", length = 30, nullable = false, unique = true)
    @Comment("이메일")
    var email: String? = null,

    @Column(name = "profile_image")
    @Comment("프로필 이미지")
    var profileImage: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    @Comment("계정 권한")
    val auth: UserRole,

    @Column(name = "access_token")
    @Comment("소셜 제공 엑세스 토큰")
    var accessToken: String? = null,

    @Column(name = "refresh_token")
    @Comment("소셜 제공 리프레시 토큰")
    var refreshToken: String? = null,

    @Column(name = "is_event")
    var isEvent: Boolean = false,

    @Column(name = "is_account_non_locked")
    @Comment("계정 잠김 유무")
    var isUserNonLocked: Boolean = true,

    @Column(name = "is_credential_non_expired")
    @Comment("비밀번호 유효기간 만료 여부")
    val isPasswordNonExpired: Boolean = true,

    @Column(name = "is_enabled")
    @Comment("블랙리스트 계정 유무")
    val isBlackListed: Boolean = true,

    @Column(name = "created_at")
    @Comment("최초 생성 일시")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    @Comment("최종 수정 일시")
    var updatedAt: LocalDateTime? = null,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_set_id")
    @Comment("사용자 세팅정보")
    var userInfo: UserInfo? = null,

): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(auth.name))
    }

    override fun getPassword(): String? {
        return userInfo?.secondPassword
    }

    override fun getUsername(): String {
        return email!!
    }
    // 잠긴 계쩡 여부
    override fun isAccountNonLocked(): Boolean {
        return isUserNonLocked
    }
    // 비밀번호 재설정 기간 만료 여부
    override fun isCredentialsNonExpired(): Boolean {
        return isPasswordNonExpired
    }
    // 블랙리스트 계쩡 여부
    override fun isEnabled(): Boolean {
        return isBlackListed
    }
}

package tech.server.reviral.api.account.model.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import tech.server.reviral.api.account.model.enums.Gender
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
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    val id: Long? = null,

    @Column(name = "login_id", length = 30, nullable = false, unique = true)
    val loginId: String,

    @Column(name = "login_pw")
    val loginPw: String,

    @Column(name = "name")
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender")
    val gender: Gender,

    @Column(name = "phone_number")
    val phone: String,

    @Column(name = "address")
    val address: String,

    @Column(name = "point_pw")
    val pointPw: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    val auth: UserRole,

    @Column(name = "is_account_non_locked")
    val isUserNonLocked: Boolean = true,

    @Column(name = "is_credential_non_expired")
    val isPasswordNonExpired: Boolean = true,

    @Column(name = "is_enabled")
    val isBlackListed: Boolean = true,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()

    ): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(auth.name))
    }

    override fun getPassword(): String {
        return loginPw
    }

    override fun getUsername(): String {
        return loginId
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

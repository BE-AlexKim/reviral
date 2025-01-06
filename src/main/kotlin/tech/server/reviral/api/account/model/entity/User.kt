package tech.server.reviral.api.account.model.entity

import jakarta.persistence.*
import org.hibernate.annotations.Comment
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
@Comment("사용자 정보 테이블")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Comment("사용자 일련번호")
    val id: Long? = null,

    @Column(name = "login_id", length = 30, nullable = false, unique = true)
    @Comment("로그인 아이디")
    val loginId: String,

    @Column(name = "login_pw")
    @Comment("비밀번호")
    val loginPw: String,

    @Column(name = "name")
    @Comment("이름")
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender")
    @Comment("성별")
    val gender: Gender,

    @Column(name = "phone_number")
    @Comment("휴대폰 번호")
    val phone: String,

    @Column(name = "address")
    @Comment("주소")
    val address: String,

    @Column(name = "point_pw")
    @Comment("포인트 전환 비밀번호")
    val pointPw: String? = null,

    @Column(name = "bank_code")
    @Comment("은행 코드")
    val bankCode: String? = null,

    @Column(name = "account_number")
    @Comment("계좌 번호")
    val account: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    @Comment("계정 권한")
    val auth: UserRole,

    @Column(name = "nv_id")
    @Comment("네이버 아이디")
    val nvId: String? = null,

    @Column(name = "cp_id")
    @Comment("쿠팡 아이디")
    val cpId: String? = null,

    @Column(name = "is_email_authorized")
    @Comment("이메일 인증처리")
    val isEmailAuthorized: Boolean = false,

    @Column(name = "is_event")
    val isEvent: Boolean = false,

    @Column(name = "is_account_non_locked")
    @Comment("계정 잠김 유무")
    val isUserNonLocked: Boolean = true,

    @Column(name = "is_credential_non_expired")
    @Comment("비밀번호 유효기간 만료 여부")
    val isPasswordNonExpired: Boolean = true,

    @Column(name = "is_enabled")
    @Comment("블랙리스트 계정 유무")
    val isBlackListed: Boolean = true,

    @Column(name = "created_at")
    @Comment("최초 생성 일시")
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

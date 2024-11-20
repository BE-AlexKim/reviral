package tech.server.reviral.api.account.model.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

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
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    val id: Long? = null,

    @Column(name = "login_id", length = 30, nullable = false, unique = true)
    val loginId: String,

    @Column(name = "login_pw")
    val loginPw: String,

    @Column(name = "point_pw")
    val pointPw: String,

    @Column(name = "phone_number")
    val phone: String,

    @Column(name = "address")
    val address: String,

    @Column(name = "user_role")
    val auth: String

): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(auth))
    }

    override fun getPassword(): String {
        return loginPw
    }

    override fun getUsername(): String {
        return loginId
    }


}

package tech.server.reviral.api.account.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.api.account.model.entity
 * fileName       : Login
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@Entity
@Table(name = "tb_login_his")
data class Login(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "login_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id")
    @JsonIgnore
    val user: User,

    @Column(name = "device_id")
    val deviceId: String,

    @Column(name = "ip_address")
    val ip: String,

    @Column(name = "create_at")
    val createAt: LocalDateTime = LocalDateTime.now()
)

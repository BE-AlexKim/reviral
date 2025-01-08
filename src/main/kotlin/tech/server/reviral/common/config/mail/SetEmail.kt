package tech.server.reviral.common.config.mail

import java.util.Random

/**
 *packageName    : tech.server.reviral.common.config.mail
 * fileName       : SetEmail
 * author         : joy58
 * date           : 2025-01-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-06        joy58       최초 생성
 */
enum class SetEmail( private val title: String, private val subject: String, private val template: String,) {
    EMAIL_AUTHORIZED("회원가입 인증 안내","[리아비럴] 회원가입 인증번호 안내", "auth"),
    PASSWORD_AUTHORIZED("비밀번호 변경 인증 안내","[리바이럴] 비밀번호 변경 인증코드 안내", "auth");

    fun getSubject() = subject
    fun template() = template

    fun values(username: String): HashMap<String, String> {
        val random = Random().nextInt(100000, 1000000).toString()
        return hashMapOf("code" to random, "username" to username, "title" to title)
    }
}
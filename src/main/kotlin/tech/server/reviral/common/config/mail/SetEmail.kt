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
enum class SetEmail(private val subject: String, private val template: String) {
    AUTHORIZED("[리아비럴] 회원가입 인증번호 안내", "auth");

    fun getSubject() = subject
    fun template() = template
    fun values(): HashMap<String, String> {
        val random = Random().nextInt(100000, 1000000).toString()
        return hashMapOf("code" to random)
    }
}
package tech.server.reviral.config.mail

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

/**
 *packageName    : tech.server.reviral.common.config.mail
 * fileName       : EmailService
 * author         : joy58
 * date           : 2025-01-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-02        joy58       최초 생성
 */
@Service
class EmailService constructor(
    private val javaMailSender: JavaMailSender,
    private val templateEngine: SpringTemplateEngine
){

    @Transactional
    @Throws(RuntimeException::class)
    fun send(to: String, subject: String, templateName: String, value: HashMap<String, String>) {

        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, Charsets.UTF_8.name())
        // 제목 설정
        helper.setSubject(subject)
        // 수신자 설정
        helper.setTo(to)
        // 데이터 설정
        val context = Context()
        value.forEach { (key, value) ->
            context.setVariable(key,value)
        }
        // 메일 내용 설정
        val html = templateEngine.process(templateName, context)
        helper.setText(html, true)

        javaMailSender.send(message)

    }

}
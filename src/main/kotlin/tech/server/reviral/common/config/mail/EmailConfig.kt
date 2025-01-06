package tech.server.reviral.common.config.mail

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.thymeleaf.TemplateEngine
import org.thymeleaf.spring6.SpringTemplateEngine
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver
import org.thymeleaf.templateresolver.StringTemplateResolver
import java.util.*


/**
 *packageName    : tech.server.reviral.common.config.mail
 * fileName       : EmailConfig
 * author         : joy58
 * date           : 2025-01-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-02        joy58       최초 생성
 */
@Configuration
@PropertySource("classpath:application.yml")
class EmailConfig (
    @Value("\${spring.mail.username}")
    private val username: String,
    @Value("\${spring.mail.password}")
    private val password: String,
    @Value("\${spring.mail.host}")
    private val host: String,
    @Value("\${spring.mail.port}")
    private val port: Int
) {


    @Bean
    fun mailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.username = username
        mailSender.password = password
        mailSender.port = port
        mailSender.javaMailProperties = getMailProperties()
        mailSender.defaultEncoding = "UTF-8"
        return mailSender
    }

    private fun getMailProperties(): Properties {
        val properties = Properties()
        properties.setProperty("mail.transport.protocol", "smtp")
        properties.setProperty("mail.smtp.auth", "true")
        properties.setProperty("mail.smtp.starttls.enable", "true")
        properties.setProperty("mail.debug", "true")
        properties.setProperty("mail.smtp.ssl.trust","smtp.gmail.com")
        properties.setProperty("mail.smtp.ssl.enable","true")
        return properties
    }

    @Bean
    fun templateEngine(): SpringTemplateEngine? {
        val templateEngine = SpringTemplateEngine()
        templateEngine.setTemplateResolver(thymeleafTemplateResolver())
        return templateEngine
    }

    @Bean
    fun thymeleafTemplateResolver(): SpringResourceTemplateResolver? {
        val templateResolver = SpringResourceTemplateResolver()
        templateResolver.prefix = "classpath:/templates/"
        templateResolver.suffix = ".html"
        templateResolver.setTemplateMode("HTML")
        templateResolver.characterEncoding = "UTF-8"
        return templateResolver
    }
}
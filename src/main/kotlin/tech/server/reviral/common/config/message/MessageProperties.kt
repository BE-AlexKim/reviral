package tech.server.reviral.common.config.message

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 *packageName    : tech.server.reviral.common.config.message
 * fileName       : MessageProperties
 * author         : joy58
 * date           : 2025-02-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-08        joy58       최초 생성
 */
@Component
@ConfigurationProperties(prefix = "message")
class MessageProperties {
    lateinit var kakao: KakaoProperties
    lateinit var nurigo: NurigoProperties


    class KakaoProperties {
        lateinit var channelId: String
    }

    class NurigoProperties {
        lateinit var apiKey: String
        lateinit var apiSecretKey: String
        lateinit var domain: String
        lateinit var from: String
        val template = Templates()
    }

    class Templates {
        lateinit var authorization: String
        lateinit var campaignStart: String
    }
}
package tech.server.reviral

import com.querydsl.core.group.GroupBy
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.account.model.entity.QUser
import tech.server.reviral.api.account.repository.AccountRepository
import tech.server.reviral.api.account.service.AccountService
import tech.server.reviral.api.campaign.model.entity.QCampaign
import tech.server.reviral.api.campaign.model.entity.QCampaignDetails
import tech.server.reviral.api.campaign.model.entity.QCampaignEnroll
import tech.server.reviral.api.campaign.model.entity.QCampaignOptions
import tech.server.reviral.api.campaign.model.entity.QCampaignSubOptions
import tech.server.reviral.api.account.model.enums.BankCode
import tech.server.reviral.api.account.model.enums.Registration
import tech.server.reviral.api.campaign.model.dto.*
import tech.server.reviral.api.campaign.model.enums.SellerStatus
import tech.server.reviral.api.campaign.repository.CampaignDetailsRepository
import tech.server.reviral.api.campaign.repository.CampaignRepository
import tech.server.reviral.api.oauth.config.OAuthProperties
import tech.server.reviral.common.config.mail.EmailService
import tech.server.reviral.common.config.message.MessageService
import tech.server.reviral.api.oauth.service.OAuthService
import tech.server.reviral.common.config.response.exception.CampaignException
import java.time.LocalDate
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral
 * fileName       : JPAQuerydslTest
 * author         : joy58
 * date           : 2024-12-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-09        joy58       최초 생성
 */
@SpringBootTest
class JPAQuerydslTest(
) {
    @Autowired
    lateinit var queryFactory: JPAQueryFactory

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var emailService: EmailService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var campaignDetailsRepository: CampaignDetailsRepository

    @Autowired
    lateinit var campaignRepository: CampaignRepository

    @Autowired
    lateinit var messageService: MessageService

    @Test
    fun sendCampaignStart() {
        val campaign = campaignRepository.findById(24)
            .orElseThrow { throw CampaignException("오류") }

        val phones = listOf("010")

//        messageService.sendCampaignStart(campaign,phones)
        messageService.sendCampaignGuides(campaign, phones)
    }

}
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
import tech.server.reviral.config.mail.EmailService
import tech.server.reviral.config.message.MessageService
import tech.server.reviral.api.oauth.service.OAuthService
import tech.server.reviral.config.response.exception.CampaignException
import tech.server.reviral.config.response.exception.enums.CampaignError
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

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
    }

    @Test
    fun workDays() {
        val startDate = LocalDate.parse("2025-02-14")
        val endDate = LocalDate.parse("2025-02-15")

        val isNotWorkDay = true

        val endDays = if ( isNotWorkDay ) {
            adjustToNearestWeekDay(endDate)
        }else {
            endDate
        }

        val daysToAdd = getLocalDateBetween(startDate,endDays, isNotWorkDay)
        println("DAY TO ADD :::::: $daysToAdd")

        val workDays = getWorkDay(startDate,daysToAdd, isNotWorkDay)
        println("WORKING DAY ::::: $workDays")
    }

    fun getWorkDay(startDate: LocalDate, targetWorkDays: Long, isNotWorkDay: Boolean): List<LocalDate> {
        val dayList = mutableListOf<LocalDate>()
        var date = startDate
        var workDayCount = 0L

        if ( isNotWorkDay ) {
            while (workDayCount < targetWorkDays) {
                if (date.dayOfWeek !in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)) {
                    dayList.add(date)
                    workDayCount++
                }
                date = date.plusDays(1) // 하루 증가
            }
        }else {
            (workDayCount..targetWorkDays).forEach { i ->
                dayList.add(date.plusDays(i))
            }
        }
        return dayList
    }

    private fun getLocalDateBetween(startDate: LocalDate, endDate: LocalDate, isNotWorkDay: Boolean): Long {
        if (startDate.isAfter(endDate)) {
            throw IllegalArgumentException("The start date must be before the end date.")
        }

        return if (isNotWorkDay) {
            var date = startDate
            var workDays = 0L

            while (!date.isAfter(endDate)) { // Iterate through dates
                if (date.dayOfWeek !in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)) {
                    workDays++
                }
                date = date.plusDays(1) // Increment day
            }
            workDays
        } else {
            ChronoUnit.DAYS.between(startDate, endDate) // Include weekends
        }
    }

    private fun adjustToNearestWeekDay(date: LocalDate): LocalDate {
        return when ( date.dayOfWeek ) {
            DayOfWeek.SATURDAY -> date.plusDays(2)
            DayOfWeek.SUNDAY -> date.plusDays(1)
            else -> date
        }
    }

}
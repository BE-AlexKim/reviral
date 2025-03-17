package tech.server.reviral.config.message

import io.github.oshai.kotlinlogging.KotlinLogging
import net.nurigo.sdk.NurigoApp
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException
import net.nurigo.sdk.message.model.KakaoOption
import net.nurigo.sdk.message.model.Message
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.server.reviral.api.account.repository.UserInfoRepository
import tech.server.reviral.api.campaign.model.entity.Campaign
import tech.server.reviral.api.campaign.model.entity.CampaignEnroll
import tech.server.reviral.config.response.exception.BasicException
import tech.server.reviral.config.response.exception.enums.BasicError
import tech.server.reviral.config.security.JwtRedisRepository
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *packageName    : tech.server.reviral.common.config.message
 * fileName       : MessageService
 * author         : joy58
 * date           : 2025-02-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-08        joy58       최초 생성
 */
@Service
class MessageService constructor(
    private val messageProperties: MessageProperties,
    private val jwtRedisRepository: JwtRedisRepository,
    private val userInfoRepository: UserInfoRepository
) {

    private val log = KotlinLogging.logger {}

    private val messageService = NurigoApp.initialize(messageProperties.nurigo.apiKey, messageProperties.nurigo.apiSecretKey, messageProperties.nurigo.domain)

    @Transactional
    @Throws(BasicException::class, NurigoMessageNotReceivedException::class)
    fun sendAuthorizationCode(phoneNumber: String): Boolean {

        // 템블릿 변수 설정
        val number = Random().nextInt(100000,999999).toString()

        // 인증번호 변수
//        val variables = mutableMapOf<String, String>()
//        variables["#{code}"] = number

        val message = Message(
            from = messageProperties.nurigo.from,
            to = phoneNumber,
            subject = "리바이럴 인증문자 발송안내",
            text = "[인증번호:$number] 리바이럴 회원가입을 인증을 위한 인증번호 입니다."
//            kakaoOptions = KakaoOption(
//                pfId = messageProperties.kakao.channelId,
//                templateId = messageProperties.nurigo.template.authorization,
//                variables = variables
//            ),
        )
        // REDIS 인증번호 데이터 저장
        jwtRedisRepository.setAuthorizationCode(phoneNumber, number, TimeUnit.MINUTES.toMillis(5))

        return try {
            messageService.send(message)
            true
        }catch (ex : NurigoMessageNotReceivedException) {
            log.info { "발송에 실패한 메세지 목록 ::::" + ex.failedMessageList }
            log.info { "오류 메세지" + ex.message }
            false
        } catch (ex: Exception) {
            log.error { "오류 메시지" + ex.message }
            throw BasicException(BasicError.DEFAULT)
        }
    }

    @Throws(BasicException::class)
    fun isValidAuthCode( phoneNumber: String, code: String ): Boolean {
        val authorizationCode = jwtRedisRepository.getAuthorizationCode(phoneNumber)
            ?: throw BasicException(BasicError.AUTHORIZED_CODE_EXPIRED)

        return if ( authorizationCode == code ) {
            jwtRedisRepository.deleteAuthorizationCode(phoneNumber)
            true
        }else {
            false
        }
    }

    @Transactional
    @Throws(BasicException::class)
    fun sendCampaignStart(campaign: Campaign, campaignErolls: List<CampaignEnroll>, phoneNumbers: List<String>): Boolean {

        val campaignTitle = campaign.campaignTitle
        val campaignPrice = campaign.campaignPrice
        val campaignPoint = campaign.reviewPoint
        val totalPrice = campaignPrice + campaignPoint

        val variables = mutableMapOf<String, String>()
        variables["#{title}"] = getTitle(campaignTitle ?: "캠페인 명 오류")
        variables["#{price}"] = NumberFormat.getNumberInstance(Locale.KOREA).format(campaignPrice)
        variables["#{point}"] = NumberFormat.getNumberInstance(Locale.KOREA).format(campaignPoint)
        variables["#{total}"] = NumberFormat.getNumberInstance(Locale.KOREA).format(totalPrice)

        val messages = phoneNumbers.map {
            Message(
                from = messageProperties.nurigo.from,
                to = it.replace("-","").trim(),
                kakaoOptions = KakaoOption(
                    pfId = messageProperties.kakao.channelId,
                    templateId = messageProperties.nurigo.template.campaignStart,
                    variables = variables
                ),
            )
        }

        sendCampaignGuides(campaign,campaignErolls, phoneNumbers)

        messageService.send(messages)

        return true
    }

    @Throws(BasicException::class)
    private fun sendCampaignGuides(campaign: Campaign, campaignEnroll: List<CampaignEnroll>, phoneNumbers: List<String?>) {

        val phones = phoneNumbers.filterNotNull().map { it.replace("-","").trim() }

        val userInfoList = userInfoRepository.findByPhoneIn(phones)

        val phoneToUserInfoMap = userInfoList.associateBy { it.phone }

        val campaignGuide = campaign.guide

        val enrollOption = campaignEnroll.associateBy{ it.user?.userInfo?.phone }

        val messages = phones.mapNotNull { phoneNumber ->
            val userInfo = phoneToUserInfoMap[phoneNumber] ?: return@mapNotNull null
            val userId = userInfo.user?.id ?: return@mapNotNull  null

            Message(
                subject = "리바이럴 구매 가이드 안내",
                from = messageProperties.nurigo.from,
                to = phoneNumber,
                text = "구매 가이드 \n\n"
                    + "구매 옵션1 :${enrollOption[phoneNumber]?.options?.title ?: "없음"} \n"
                    + "구매 옵션2 :${enrollOption[phoneNumber]?.subOptions?.title ?: "없음"} \n"
                    + "구매 개수 : 1개 \n\n"
                    + "준수 사항 \n"
                    + "글자 수 : ${campaignGuide?.textLength ?: "없음"} \n"
                    + "등록 조건 : ${campaignGuide?.condition ?: "없음"} \n"
                    + "기타 사항 : ${campaignGuide?.etcText ?: "없음"} \n"
                    + "강조 내용 : \n${campaignGuide?.strengthText ?: "없음"} \n\n"
                    + "구매 링크 : ${campaign.campaignUrl} \n\n"
                    + "등록 링크 : \nhttps://reviral.kr/campaign \n"
            )
        }
        messageService.send(messages)
    }

    private fun getHtmlToText(input: String): String {
        return Jsoup.parse(input).text() + "\n"
    }

    private fun getTitle(campaignTitle: String): String {
        val length = campaignTitle.length
        return if ( length > 10 ) {
            campaignTitle.substring(0,10)
        }else {
            campaignTitle
        }
    }
}
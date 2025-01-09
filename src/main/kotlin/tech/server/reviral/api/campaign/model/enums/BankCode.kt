package tech.server.reviral.api.campaign.model.enums

/**
 *packageName    : tech.server.reviral.api.campaign.model.enums
 * fileName       : BankCode
 * author         : joy58
 * date           : 2025-01-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-09        joy58       최초 생성
 */
enum class BankCode(private val bankCode: String, private val accountFormat: String, private val bankName: String) {
    IBK("003", "AAA-BBBBBB-CC-DDD", "IBK 기업은행"),
    KEB("081", "AAA-BBBBBB-CCCCC", "KEB 하나은행"),
    KB1("004", "AAA-BB-CCCC-DDD","(구)국민은행"),
    KB2("004", "AAAAAA-BB-CCCCCC","국민은행"),
    NH("011", "AAA-BBBB-CCCC-DD","NH 농협은행"),
    SC("023", "AAA-BB-CCCCCC","SC 제일은행"),
    SIN1("088","AAA-BB-CCCCCC","(구)신한은행"),
    SIN2("088", "AAA-BBB-CCCCCC","신한은행"),
    CITY("027","AAA-BBBBBB-CCC","씨티은행"),
    WOO("020","AAAA-BBB-CCCCCC","우리은행"),
    KKO("090","AAAA-BB-CCCCCCC","카카오뱅크"),
    K_BANK("089","AAA-BBB-CCCCCC","케이뱅크");
}
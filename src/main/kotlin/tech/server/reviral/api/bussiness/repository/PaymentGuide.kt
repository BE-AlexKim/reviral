package tech.server.reviral.api.bussiness.repository

/**
 *packageName    : tech.server.reviral.api.bussiness.repository
 * fileName       : JoinGuide
 * author         : joy58
 * date           : 2025-02-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-12        joy58       최초 생성
 */
interface PaymentGuide {

    fun firstOption(): String

    fun secondOption(): String?

    fun countText(): String

    fun lengthText(): String?

    fun strengthText(): String?

    fun conditionText(): String?

    fun etcText(): String?

}
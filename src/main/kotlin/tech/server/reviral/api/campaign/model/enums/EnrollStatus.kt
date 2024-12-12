package tech.server.reviral.api.campaign.model.enums

/**
 *packageName    : tech.server.reviral.api.campaign.model.enums
 * fileName       : EnrollStatus
 * author         : joy58
 * date           : 2024-12-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-11        joy58       최초 생성
 */
enum class EnrollStatus(private val desc: String) {
    APPLY("참여 진행중"),PROGRESS("진행"),REVIEW("후기 작성"),INSPECT("검수"),COMPLETE("완료")
}
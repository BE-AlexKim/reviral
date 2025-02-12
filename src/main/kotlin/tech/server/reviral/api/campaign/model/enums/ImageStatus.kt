package tech.server.reviral.api.campaign.model.enums

/**
 *packageName    : tech.server.reviral.api.campaign.model.enums
 * fileName       : ImageStatus
 * author         : joy58
 * date           : 2025-01-31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-31        joy58       최초 생성
 */
enum class ImageStatus(private val desc: String){
    REGISTER("등록"),EDIT("재등록"),INSPECT("검수중"),COMPLETE("완료");
    fun description() = desc
}

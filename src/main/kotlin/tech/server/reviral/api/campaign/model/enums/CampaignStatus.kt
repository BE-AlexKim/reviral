package tech.server.reviral.api.campaign.model.enums

import com.fasterxml.jackson.annotation.JsonFormat

/**
 * packageName    : tech.server.reviral.api.campaign.model.enums
 * fileName       : CampaignStatus
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@JsonFormat(shape = JsonFormat.Shape.STRING)
enum class CampaignStatus(private val desc: String) {
    WAIT("입금대기"),PROGRESS("진행중"),FINISH("마감");
}
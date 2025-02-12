package tech.server.reviral.api.campaign.model.enums

import com.fasterxml.jackson.annotation.JsonFormat

/**
 *packageName    : tech.server.reviral.api.campaign.model.enums
 * fileName       : CampaignCategory
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@JsonFormat(shape = JsonFormat.Shape.STRING)
enum class CampaignCategory(private val desc: String) {
    AB("AIR BOX"), PT("PHOTO"), EX("EXPERIENCE");

    fun desc() = desc

}
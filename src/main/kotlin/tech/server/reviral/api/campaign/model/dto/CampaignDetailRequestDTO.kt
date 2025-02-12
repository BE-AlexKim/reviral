package tech.server.reviral.api.campaign.model.dto

import com.fasterxml.jackson.annotation.JsonAlias
import java.time.LocalDate

/**
 *packageName    : tech.server.reviral.api.campaign.model.entity
 * fileName       : CampaignDetailRequestDTO
 * author         : joy58
 * date           : 2025-01-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-22        joy58       최초 생성
 */
data class CampaignDetailRequestDTO(
    @JsonAlias("cd_ix")
    val encodeUrl: String,
    @JsonAlias("dt")
    val applyDate: LocalDate

)

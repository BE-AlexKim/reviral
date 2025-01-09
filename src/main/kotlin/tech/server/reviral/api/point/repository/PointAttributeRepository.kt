package tech.server.reviral.api.point.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import tech.server.reviral.api.account.model.entity.User
import tech.server.reviral.api.campaign.model.entity.CampaignEnroll
import tech.server.reviral.api.point.model.entity.PointAttribute
import tech.server.reviral.api.point.model.enums.PointStatus

/**
 *packageName    : tech.server.reviral.api.point.repository
 * fileName       : PointHistoryRepository
 * author         : joy58
 * date           : 2024-12-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-13        joy58       최초 생성
 */
@Repository
interface PointAttributeRepository: JpaRepository<PointAttribute, Long> {

    fun findByUser(user: User): PointAttribute

    fun findByCampaignEnroll(enroll: CampaignEnroll): PointAttribute?

    fun deleteByCampaignEnroll(campaignEnroll: CampaignEnroll)

}
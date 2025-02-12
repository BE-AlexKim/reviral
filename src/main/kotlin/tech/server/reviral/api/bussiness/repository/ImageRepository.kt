package tech.server.reviral.api.bussiness.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tech.server.reviral.api.bussiness.model.entity.Image

/**
 *packageName    : tech.server.reviral.api.bussiness.repository
 * fileName       : ImageRepository
 * author         : joy58
 * date           : 2025-02-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-03        joy58       최초 생성
 */
@Repository
interface ImageRepository: JpaRepository<Image, Long> {
}
package tech.server.reviral.view.home.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 *packageName    : tech.server.reviral.view.home.service
 * fileName       : HomeService
 * author         : joy58
 * date           : 2025-02-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-16        joy58       최초 생성
 */
@Service
class IndexService{

    @Transactional
    fun findAll(pageable: Pageable): Page<Map<String, Any>> {

    }



}
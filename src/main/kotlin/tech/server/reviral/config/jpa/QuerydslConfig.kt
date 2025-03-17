package tech.server.reviral.config.jpa

import com.querydsl.jpa.JPQLTemplates
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 *packageName    : tech.server.reviral.common.config.jpa
 * fileName       : QueryFactory
 * author         : joy58
 * date           : 2024-12-09
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-09        joy58       최초 생성
 */
@Configuration
class QuerydslConfig {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Bean
    fun queryFactory(): JPAQueryFactory {
        return JPAQueryFactory(JPQLTemplates.DEFAULT,entityManager)
    }
}
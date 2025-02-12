package tech.server.reviral.common.config.security

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

/**
 *packageName    : tech.server.reviral.common.config.security
 * fileName       : JwtRedisRepository
 * author         : joy58
 * date           : 2024-11-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-28        joy58       최초 생성
 */
@Service
class JwtRedisRepository constructor(
    private val redisTemplate: RedisTemplate<String, String>
) {

    @Transactional
    fun set(userId: String, token: String, expiration: Long) {
        redisTemplate.opsForValue().set(userId, token, expiration, TimeUnit.MILLISECONDS)
    }

    @Transactional
    fun get(userId: String): String? {
        return redisTemplate.opsForValue().get(userId)
    }

    @Transactional
    fun delete(userId: String) {
        redisTemplate.delete(userId)
    }

    @Transactional
    fun setAuthorizationCode(key: String, code: String, expiration: Long) {
        redisTemplate.opsForValue().set(key, code, expiration, TimeUnit.MILLISECONDS)
    }

    @Transactional
    fun getAuthorizationCode(key: String): String? {
        return redisTemplate.opsForValue().get(key)
    }

    @Transactional
    fun deleteAuthorizationCode(email: String) {
        redisTemplate.delete("authorized_$email")
    }

}
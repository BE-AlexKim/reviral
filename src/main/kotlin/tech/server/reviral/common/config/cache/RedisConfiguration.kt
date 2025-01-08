package tech.server.reviral.common.config.cache

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.serializer.StringRedisSerializer

import org.springframework.data.redis.core.RedisTemplate

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

import org.springframework.data.redis.connection.RedisStandaloneConfiguration

import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer


/**
 *packageName    : tech.server.reviral.common.config.cache
 * fileName       : RedisConfiguration
 * author         : joy58
 * date           : 2024-11-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-28        joy58       최초 생성
 */
@Configuration
@EnableRedisRepositories
class RedisConfiguration(
    @Value("\${spring.data.redis.host}")
    private val host: String,

    @Value("\${spring.data.redis.port}")
    private val port: Int,

    @Value("\${spring.data.redis.password}")
    private val password: String
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory? {
        val redisConfiguration = RedisStandaloneConfiguration()
        redisConfiguration.hostName = host
        redisConfiguration.port = port
        redisConfiguration.setPassword(password)
        return LettuceConnectionFactory(redisConfiguration)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, String>? {
        val redisTemplate = RedisTemplate<String, String>()
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = StringRedisSerializer() // Json 포맷으로 저장
        redisTemplate.connectionFactory = redisConnectionFactory()
        return redisTemplate
    }



}
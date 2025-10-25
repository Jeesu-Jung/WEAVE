package io.jeesu.grovetaskmixtureservice.infrastructure.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration

@Configuration
class CacheConfig {
    @Primary
    @Bean
    fun jsonCacheInfinity(redisConnectionFactory: RedisConnectionFactory): CacheManager =
        buildRedisCacheManager(redisConnectionFactory, Duration.ZERO)

    private fun buildRedisCacheManager(
        redisConnectionFactory: RedisConnectionFactory,
        ttl: Duration
    ): RedisCacheManager {
        val objectMapper = buildObjectMapper()
        val configuration = RedisCacheConfiguration
            .defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer(objectMapper))
            )
            .entryTtl(ttl)
        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(configuration).build()
    }

    private fun buildObjectMapper(): ObjectMapper {
        return ObjectMapper()
            .registerModule(JavaTimeModule())
            .registerModules(
                KotlinModule
                    .Builder()
                    .build()
            )
            .activateDefaultTyping(
                BasicPolymorphicTypeValidator
                    .builder()
                    .allowIfBaseType(Any::class.java)
                    .build(), ObjectMapper.DefaultTyping.EVERYTHING
            )
    }
}

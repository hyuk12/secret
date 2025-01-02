package com.study.apigatewaykotlin.config

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Mono

@Configuration
class RateLimitConfig {
    @Bean
    fun redisRateLimiter(): RedisRateLimiter {
        return RedisRateLimiter(10, 20)
    }

    @Bean
    fun userKeyResolver(): KeyResolver { // 메서드 이름 변경
        return KeyResolver { exchange ->
            val userId = exchange.request.headers["X-User-ID"]?.firstOrNull()
            Mono.just(
                userId ?: exchange.request.remoteAddress?.address?.hostAddress
                ?: throw IllegalArgumentException("Cannot resolve key for rate limiting")
            )
        }
    }
}

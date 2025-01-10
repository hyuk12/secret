package com.study.couponcore.configuration

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedisConfiguration {
    @Value("\${spring.redis.host}")
    lateinit var host: String
    @Value("\${spring.redis.port}")
    lateinit var port: String

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        val address = "redis://$host:$port"
        config.useSingleServer().setAddress(address)
        return Redisson.create(config)
    }
}
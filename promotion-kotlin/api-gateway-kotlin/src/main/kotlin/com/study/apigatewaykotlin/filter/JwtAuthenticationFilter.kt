package com.study.apigatewaykotlin.filter

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationFilter(
    lbFunction: ReactorLoadBalancerExchangeFilterFunction
) : AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config>(Config::class.java) {

    @LoadBalanced
    private val webClient: WebClient = WebClient.builder()
        .filter(lbFunction)
        .baseUrl("http://user-service-kotlin")
        .build()

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val authHeader = exchange.request.headers["Authorization"]?.firstOrNull()
            if (!authHeader.isNullOrEmpty() && authHeader.startsWith("Bearer ")) {
                val token = authHeader.substring(7)
                return@GatewayFilter validateToken(token)
                    .flatMap { userId -> proceedWithUserId(userId, exchange, chain) }
                    .switchIfEmpty(chain.filter(exchange)) // Invalid token
                    .onErrorResume { e -> handleAuthenticationError(exchange, e) } // Error handling
            }
            chain.filter(exchange)
        }
    }

    private fun handleAuthenticationError(exchange: ServerWebExchange, e: Throwable): Mono<Void> {
        exchange.response.statusCode = HttpStatus.UNAUTHORIZED
        return exchange.response.setComplete()
    }

    private fun validateToken(token: String): Mono<Long> {
        return webClient.post()
            .uri("/api/v1/users/validate-token")
            .bodyValue(mapOf("token" to token))
            .header("Content-Type", "application/json")
            .retrieve()
            .bodyToMono<Map<String, Any>>()
            .map { response -> response["id"].toString().toLong() }
    }

    private fun proceedWithUserId(userId: Long, exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        exchange.request.mutate().header("X-USER-ID", userId.toString()).build()
        return chain.filter(exchange)
    }

    class Config {
        // 필터 구성을 위한 설정 클래스
    }
}

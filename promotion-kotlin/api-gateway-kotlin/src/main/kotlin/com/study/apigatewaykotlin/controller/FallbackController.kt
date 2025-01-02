package com.study.apigatewaykotlin.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/fallback")
class FallbackController {

    @GetMapping("/users")
    fun userFallback(): Mono<Map<String, *>> {
        return Mono.just(mapOf(Pair("status", "down")))
    }
}

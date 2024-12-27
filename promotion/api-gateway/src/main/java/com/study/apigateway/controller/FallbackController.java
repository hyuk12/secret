package com.study.apigateway.controller;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/fallback")
public class FallbackController {
  @GetMapping("/users")
  public Mono<Map<String, Object>> userFallback() {
    return Mono.just(Map.of("status", "down"));
  }
}
package com.study.apigatewaykotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class ApiGatewayKotlinApplication

fun main(args: Array<String>) {
    runApplication<ApiGatewayKotlinApplication>(*args)
}

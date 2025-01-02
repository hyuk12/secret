package com.study.discoveryservicekotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
@EnableEurekaServer
class DiscoveryServiceKotlinApplication

fun main(args: Array<String>) {
    runApplication<DiscoveryServiceKotlinApplication>(*args)
}

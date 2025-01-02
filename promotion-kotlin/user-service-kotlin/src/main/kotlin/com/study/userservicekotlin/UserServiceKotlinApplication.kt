package com.study.userservicekotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class UserServiceKotlinApplication

fun main(args: Array<String>) {
    runApplication<UserServiceKotlinApplication>(*args)
}

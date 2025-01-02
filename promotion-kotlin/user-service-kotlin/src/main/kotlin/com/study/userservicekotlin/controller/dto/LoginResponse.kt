package com.study.userservicekotlin.controller.dto

data class LoginResponse(
    val token: String,
    val user: Response
)

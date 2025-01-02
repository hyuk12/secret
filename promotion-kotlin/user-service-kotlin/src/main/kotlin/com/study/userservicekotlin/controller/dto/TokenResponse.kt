package com.study.userservicekotlin.controller.dto

data class TokenResponse(
    val email: String,
    val valid: Boolean,
    val role: String,
)

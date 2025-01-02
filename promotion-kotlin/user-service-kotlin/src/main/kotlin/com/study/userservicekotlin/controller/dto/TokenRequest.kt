package com.study.userservicekotlin.controller.dto

import jakarta.validation.constraints.NotBlank

data class TokenRequest(
    @NotBlank(message = "Token is required")
    val token: String,
)

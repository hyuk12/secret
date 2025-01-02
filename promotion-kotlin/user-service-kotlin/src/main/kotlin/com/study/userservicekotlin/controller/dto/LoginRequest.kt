package com.study.userservicekotlin.controller.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    val email: String,

    @NotBlank(message = "Password is required")
    val password: String,
)

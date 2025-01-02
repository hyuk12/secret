package com.study.userservicekotlin.controller.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignupRequest(
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    val email: String,

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
        message = "Password must be at least 8 characters long and contain both letters and numbers")
    val password: String,

    @NotBlank(message = "Name is required")
    val name: String,
)

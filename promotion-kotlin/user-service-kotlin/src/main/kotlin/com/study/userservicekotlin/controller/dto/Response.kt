package com.study.userservicekotlin.controller.dto

import com.study.userservicekotlin.entity.User
import java.time.LocalDateTime

data class Response(
    val id: Long,
    val email: String,
    val name: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
)

fun User.toResponse() = Response(
    id = this.id,
    email = this.email,
    name = this.name,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
)


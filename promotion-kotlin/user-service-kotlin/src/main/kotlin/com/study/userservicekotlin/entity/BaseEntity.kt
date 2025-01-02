package com.study.userservicekotlin.entity

import java.time.LocalDateTime

abstract class BaseEntity(

) {
    val createdAt: LocalDateTime = LocalDateTime.now()

    var updatedAt: LocalDateTime? = null
        protected set

    fun updatedAt() {
        this.updatedAt = LocalDateTime.now()
    }
}

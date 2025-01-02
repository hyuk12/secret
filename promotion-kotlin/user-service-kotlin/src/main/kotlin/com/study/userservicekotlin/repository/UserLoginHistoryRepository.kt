package com.study.userservicekotlin.repository

import com.study.userservicekotlin.entity.User
import com.study.userservicekotlin.entity.UserLoginHistory
import org.springframework.data.jpa.repository.JpaRepository

interface UserLoginHistoryRepository: JpaRepository<UserLoginHistory, Long> {
    fun findByUserOrderByLoginTimeDesc(user: User): List<UserLoginHistory>
}

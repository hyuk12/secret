package com.study.couponservice.repository

import com.study.couponservice.domain.Coupon
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface CouponRepository: JpaRepository<Coupon, Long> {
    fun findByIdAndUserId(id: Long, userId: Long): Optional<Coupon>

    @Query("SELECT COUNT(c) FROM Coupon c WHERE c.couponPolicy.id = :policyId")
    fun countByCouponPolicyId(@Param("policyId") policyId: Long): Long
}

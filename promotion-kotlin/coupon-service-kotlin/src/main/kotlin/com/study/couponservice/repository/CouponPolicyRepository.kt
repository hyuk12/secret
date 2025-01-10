package com.study.couponservice.repository

import com.study.couponservice.domain.CouponPolicy
import org.springframework.data.jpa.repository.JpaRepository

interface CouponPolicyRepository: JpaRepository<CouponPolicy, Long> {
}

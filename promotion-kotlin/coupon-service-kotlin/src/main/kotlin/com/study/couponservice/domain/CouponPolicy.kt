package com.study.couponservice.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import lombok.Setter
import java.time.LocalDateTime

@Entity
@Table(name = "coupon_policies")
class CouponPolicy(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column
    val description: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val discountType: DiscountType,

    @Column(nullable = false)
    val discountValue: Int,

    @Column(nullable = false)
    val minimumOrderAmount: Int,

    @Column(nullable = false)
    val maximumDiscountAmount: Int,

    @Column(nullable = false)
    val totalQuantity: Int,

    @Column(nullable = false)
    val startTime: LocalDateTime,

    @Column(nullable = false)
    var endTime: LocalDateTime,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    enum class DiscountType {
        FIXED_AMOUNT,    // 정액 할인
        PERCENTAGE       // 정률 할인
    }

    @PrePersist
    fun onCreate() {
        updatedAt = createdAt
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }

    fun isValidPeriod(): Boolean {
        val now = LocalDateTime.now()
        return now.isAfter(startTime) && now.isBefore(endTime)
    }
}

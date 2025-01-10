package com.study.couponservice.domain

import com.study.couponservice.exception.CouponAlreadyUsedException
import com.study.couponservice.exception.CouponExpiredException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity
class Coupon(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id")
    val couponPolicy: CouponPolicy,

    val userId: Long,
    val couponCode: String,

    @Enumerated(EnumType.STRING)
    var status: Status = Status.AVAILABLE,

    var orderId: Long? = null,
    var usedAt: LocalDateTime? = null,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    enum class Status {
        AVAILABLE,
        USED,
        EXPIRED,
        CANCELLED
    }

    fun use(orderId: Long) {
        when {
            status == Status.USED -> throw CouponAlreadyUsedException("이미 사용된 쿠폰입니다.")
            isExpired() -> throw CouponExpiredException("만료된 쿠폰입니다.")
            else -> {
                this.status = Status.USED
                this.orderId = orderId
                this.usedAt = LocalDateTime.now()
            }
        }
    }

    fun cancel() {
        if (status != Status.USED) {
            throw IllegalStateException("사용되지 않은 쿠폰입니다.")
        }
        this.status = Status.CANCELLED
        this.orderId = null
        this.usedAt = null
    }

    fun isExpired(): Boolean {
        val now = LocalDateTime.now()
        return now.isBefore(couponPolicy.startTime) || now.isAfter(couponPolicy.endTime)
    }

    fun isUsed(): Boolean {
        return status == Status.USED
    }
}

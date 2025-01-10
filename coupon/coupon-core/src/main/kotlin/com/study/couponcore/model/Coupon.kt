package com.study.couponcore.model

import com.study.couponcore.exception.CouponIssueException
import com.study.couponcore.exception.ErrorCode
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity
@Table(name = "coupons")
@NoArgsConstructor
@AllArgsConstructor
class Coupon (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val couponType: CouponType,

    val totalQuantity: Int? = null,

    @Column(nullable = false)
    var issuedQuantity: Int = 0,

    @Column(nullable = false)
    val discountAmount: Int,

    @Column(nullable = false)
    val minAvailableAmount: Int,

    @Column(nullable = false)
    val dateIssueStart: LocalDateTime,

    @Column(nullable = false)
    val dateIssueEnd: LocalDateTime
): BaseTimeEntity() {
    constructor() : this() {

    }


    fun availableIssueQuantity(): Boolean {
        return totalQuantity?.let { it > issuedQuantity } ?: true
    }

    fun availableIssueDate(): Boolean {
        val now = LocalDateTime.now()
        return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now)
    }

    fun isIssueComplete(): Boolean {
        val now = LocalDateTime.now()
        return dateIssueEnd.isBefore(now) || !availableIssueQuantity()
    }

    fun issue() {
        if (!availableIssueQuantity()) {
            throw CouponIssueException(
                ErrorCode.INVALID_COUPON_ISSUE_QUANTITY,
                "발급 가능한 수량을 초과합니다. total: $totalQuantity, issued: $issuedQuantity"
            )
        }
        if (!availableIssueDate()) {
            throw CouponIssueException(
                ErrorCode.INVALID_COUPON_ISSUE_DATE,
                "발급 가능한 일자가 아닙니다. request: ${LocalDateTime.now()}, issueStart: $dateIssueStart, issueEnd: $dateIssueEnd"
            )
        }
        issuedQuantity++
    }
}
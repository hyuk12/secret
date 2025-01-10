package com.study.couponservice.service.v1

import CouponPolicyDto
import com.study.couponservice.domain.CouponPolicy
import com.study.couponservice.exception.CouponPolicyNotFoundException
import com.study.couponservice.repository.CouponPolicyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponPolicyService(
    private val couponPolicyRepository: CouponPolicyRepository,
) {

    @Transactional
    fun createCouponPolicy(request: CouponPolicyDto.CreateRequest): CouponPolicy {
        val couponPolicy = request.toEntity()
        return couponPolicyRepository.save(couponPolicy)
    }

    @Transactional(readOnly = true)
    fun getCouponPolicy(id: Long): CouponPolicy {
        return couponPolicyRepository.findByIdOrNull(id) ?: throw CouponPolicyNotFoundException("쿠폰 정책을 찾을 수 없습니다.")
    }

    @Transactional(readOnly = true)
    fun getAllCouponPolicies(): List<CouponPolicy> {
        return couponPolicyRepository.findAll()
    }
}

package org.study.couponservice.service.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.couponservice.domain.CouponPolicy;
import org.study.couponservice.dto.v1.CouponPolicyDto;
import org.study.couponservice.exception.CouponPolicyNotFoundException;
import org.study.couponservice.repository.CouponPolicyRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponPolicyService {
    private final CouponPolicyRepository couponPolicyRepository;

    @Transactional
    public CouponPolicy createCouponPolicy(CouponPolicyDto.CreateRequest request) {
        CouponPolicy couponPolicy = request.toEntity();
        return couponPolicyRepository.save(couponPolicy);
    }

    @Transactional(readOnly = true)
    public CouponPolicy getCouponPolicy(Long id) {
        return couponPolicyRepository.findById(id)
                .orElseThrow(() -> new CouponPolicyNotFoundException("쿠폰 정책을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<CouponPolicy> getCouponPolicies() {
        return couponPolicyRepository.findAll();
    }
}

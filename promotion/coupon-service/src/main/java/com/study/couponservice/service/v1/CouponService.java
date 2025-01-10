package com.study.couponservice.service.v1;

import com.study.couponservice.config.UserIdInterceptor;
import com.study.couponservice.domain.Coupon;
import com.study.couponservice.domain.CouponPolicy;
import com.study.couponservice.dto.v1.CouponDto;
import com.study.couponservice.exception.CouponIssueException;
import com.study.couponservice.exception.CouponNotFoundException;
import com.study.couponservice.repository.CouponPolicyRepository;
import com.study.couponservice.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponPolicyRepository couponPolicyRepository;

    @Transactional
    public Coupon issueCoupon(CouponDto.IssueRequest request) {
        CouponPolicy couponPolicy = couponPolicyRepository.findByIdWithLock(request.couponPolicyId())
                .orElseThrow(() -> new CouponIssueException("쿠폰 정책을 찾을 수 없습니다."));

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(couponPolicy.getStartTime()) || now.isAfter(couponPolicy.getEndTime())) {
            throw new CouponIssueException("쿠폰 발급 기간이 아닙니다.");
        }

        long issuedCouponCount = couponRepository.countByCouponPolicyId(couponPolicy.getId());
        if (issuedCouponCount >= couponPolicy.getTotalQuantity()) {
            throw new CouponIssueException("쿠폰이 모두 소진되었습니다.");
        }

        Coupon coupon = Coupon.builder()
                .couponPolicy(couponPolicy)
                .userId(UserIdInterceptor.getCurrentUserId())
                .couponCode(generateCouponCode())
                .build();

        return couponRepository.save(coupon);
    }

    private String generateCouponCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    @Transactional
    public Coupon useCoupon(Long couponId, Long orderId) {
        Long currentUserId = UserIdInterceptor.getCurrentUserId();

        Coupon coupon = couponRepository.findByIdAndUserId(couponId, currentUserId)
                .orElseThrow(() -> new CouponNotFoundException("쿠폰을 찾을 수 없거나 접근 권한이 없습니다."));

        coupon.use(orderId);
        return coupon;
    }

    @Transactional
    public Coupon cancelCoupon(Long couponId) {
        Long currentUserId = UserIdInterceptor.getCurrentUserId();

        Coupon coupon = couponRepository.findByIdAndUserId(couponId, currentUserId)
                .orElseThrow(() -> new CouponNotFoundException("쿠폰을 찾을 수 없거나 접근 권한이 없습니다."));

        coupon.cancel();
        return coupon;
    }

    @Transactional(readOnly = true)
    public List<CouponDto.Response> getCoupons(CouponDto.ListRequest request) {
        Long currentUserId = UserIdInterceptor.getCurrentUserId();
        return couponRepository.findByUserIdAndStatusOrderByCreatedAtDesc(
                currentUserId,
                request.status(),
                PageRequest.of(
                        request.page() != null ? request.page() : 0,
                        request.size() != null ? request.size() : 10
                )
        ).stream()
        .map(CouponDto.Response::from)
        .toList();
    }
}

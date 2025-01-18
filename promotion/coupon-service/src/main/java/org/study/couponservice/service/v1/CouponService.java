package org.study.couponservice.service.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.couponservice.config.UserIdInterceptor;
import org.study.couponservice.domain.Coupon;
import org.study.couponservice.domain.CouponPolicy;
import org.study.couponservice.dto.CouponDto;
import org.study.couponservice.exception.CouponIssueException;
import org.study.couponservice.exception.CouponNotFoundException;
import org.study.couponservice.repository.CouponPolicyRepository;
import org.study.couponservice.repository.CouponRepository;

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
            throw new CouponIssueException("쿠폰 발급 기간이 아닙니다");
        }

        long issuedCouponCount = couponRepository.countByCouponPolicyId(couponPolicy.getId());
        if (issuedCouponCount >= couponPolicy.getTotalQuantity()) {
            throw new CouponIssueException("쿠폰이 모두 소진되었습니다");
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
        Long userId = UserIdInterceptor.getCurrentUserId();

        Coupon coupon = couponRepository.findByIdAndUserId(couponId, userId)
                .orElseThrow(() -> new CouponNotFoundException("쿠폰을 찾을 수 없거나 접근 권한이 없습니다."));

        coupon.use(orderId);
        return coupon;
    }

    @Transactional
    public Coupon cancelCoupon(Long couponId) {
        Long userId = UserIdInterceptor.getCurrentUserId();

        Coupon coupon = couponRepository.findByIdAndUserId(couponId, userId)
                .orElseThrow(() -> new CouponNotFoundException("쿠폰을 찾을 수 없거나 접근 권한이 없습니다."));

        coupon.cancel();
        return coupon;
    }

    @Transactional(readOnly = true)
    public List<CouponDto.Response> getCoupons(CouponDto.ListRequest request) {
        Long userId = UserIdInterceptor.getCurrentUserId();
        return couponRepository.findByUserIdAndStatusOrderByCreatedAtDesc(
                userId,
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

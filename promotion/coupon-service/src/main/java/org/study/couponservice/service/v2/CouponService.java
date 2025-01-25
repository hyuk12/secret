package org.study.couponservice.service.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.couponservice.domain.Coupon;
import org.study.couponservice.dto.v2.CouponDto;
import org.study.couponservice.exception.CouponNotFoundException;
import org.study.couponservice.repository.CouponRepository;

@Service("couponServiceV2")
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponRedisService couponRedisService;
    private final CouponStateService couponStateService;

    @Transactional
    public CouponDto.CouponResponse issueCoupon(CouponDto.CouponIssueRequest request) {
        Coupon coupon = couponRedisService.issueCoupon(request);
        couponStateService.updateCouponState(couponRepository.findById(coupon.getId())
                .orElseThrow(() -> new CouponNotFoundException("쿠폰을 찾을 수 없습니다.")));

        return CouponDto.CouponResponse.from(coupon);
    }

    @Transactional
    public CouponDto.CouponResponse useCoupon(Long couponId, Long orderId) {
        Coupon coupon = couponRepository.findByIdWithLock(couponId)
                .orElseThrow(() -> new CouponNotFoundException("쿠폰을 찾을 수 없습니다."));

        coupon.use(orderId);
        couponStateService.updateCouponState(coupon);

        return CouponDto.CouponResponse.from(coupon);
    }

    @Transactional
    public CouponDto.CouponResponse cancelCoupon(Long couponId) {
        Coupon coupon = couponRepository.findByIdWithLock(couponId)
                .orElseThrow(() -> new CouponNotFoundException("쿠폰을 찾을 수 없습니다."));

        coupon.cancel();
        couponStateService.updateCouponState(coupon);

        return CouponDto.CouponResponse.from(coupon);
    }
}

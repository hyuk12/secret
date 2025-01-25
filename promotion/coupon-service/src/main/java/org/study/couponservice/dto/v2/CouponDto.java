package org.study.couponservice.dto.v2;

import lombok.Builder;
import org.study.couponservice.domain.Coupon;
import org.study.couponservice.domain.CouponPolicy;

import java.time.LocalDateTime;

public record CouponDto() {

    @Builder
    public record CouponIssueRequest(
        Long couponPolicyId,
        Long userId
    ){}

    @Builder
    public record CouponResponse(
            Long id,
            Long userId,
            String couponCode,
            CouponPolicy.DiscountType discountType,
            int discountValue,
            int minimumOrderAmount,
            int maximumDiscountAmount,
            LocalDateTime validFrom,
            LocalDateTime validUntil,
            boolean used,
            Long orderId,
            LocalDateTime usedAt
    ){
        public static CouponResponse from(Coupon coupon) {
            CouponPolicy couponPolicy = coupon.getCouponPolicy();
            return CouponResponse.builder()
                    .id(coupon.getId())
                    .userId(coupon.getUserId())
                    .couponCode(coupon.getCouponCode())
                    .discountType(couponPolicy.getDiscountType())
                    .discountValue(couponPolicy.getDiscountValue())
                    .minimumOrderAmount(couponPolicy.getMinimumOrderAmount())
                    .maximumDiscountAmount(couponPolicy.getMaximumDiscountAmount())
                    .validFrom(couponPolicy.getStartTime())
                    .validUntil(couponPolicy.getEndTime())
                    .used(coupon.isUsed())
                    .orderId(coupon.getOrderId())
                    .usedAt(coupon.getUsedAt())
                    .build();
        }
    }

    @Builder
    public record CouponUseRequest(
            Long orderId,
            Integer orderAmount
    ){}
}

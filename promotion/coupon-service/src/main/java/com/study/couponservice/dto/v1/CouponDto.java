package com.study.couponservice.dto.v1;

import com.study.couponservice.domain.Coupon;
import com.study.couponservice.domain.CouponPolicy;
import lombok.Builder;

import java.time.LocalDateTime;

public record CouponDto(
) {

    @Builder
    public static record IssueRequest(
            Long couponPolicyId
    ){}

    public static record UseRequest(
            Long orderId
    ){}

    @Builder
    public static record ListRequest(
            Coupon.Status status,
            Integer page,
            Integer size
    ){}

    @Builder
    public static record Response(
            Long id,
            Long userId,
            String couponCode,
            CouponPolicy.DiscountType discountType,
            int discountValue,
            int minimumOrderAmount,
            int maximumDiscountAmount,
            LocalDateTime validFrom,
            LocalDateTime validUntil,
            Coupon.Status status,
            Long orderId,
            LocalDateTime usedAt
    ){
        public static Response from(Coupon coupon) {
            CouponPolicy policy = coupon.getCouponPolicy();
            return Response.builder()
                    .id(coupon.getId())
                    .userId(coupon.getUserId())
                    .couponCode(coupon.getCouponCode())
                    .discountType(policy.getDiscountType())
                    .discountValue(policy.getDiscountValue())
                    .minimumOrderAmount(policy.getMinimumOrderAmount())
                    .maximumDiscountAmount(policy.getMaximumDiscountAmount())
                    .validFrom(policy.getStartTime())
                    .validUntil(policy.getEndTime())
                    .status(coupon.getStatus())
                    .orderId(coupon.getOrderId())
                    .usedAt(coupon.getUsedAt())
                    .build();
        }
    }
}

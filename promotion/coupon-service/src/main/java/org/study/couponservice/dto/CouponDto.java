package org.study.couponservice.dto;

import lombok.Builder;
import org.study.couponservice.domain.Coupon;
import org.study.couponservice.domain.CouponPolicy;

import java.time.LocalDateTime;

public record CouponDto() {

    @Builder
    public record IssueRequest(
            Long couponPolicyId
    ) {}

    @Builder
    public record ListRequest(
            Coupon.Status status,
            Integer page,
            Integer size
    ) {}

    @Builder
    public record Response(
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
    ) {
        public static Response from(Coupon coupon) {
            CouponPolicy couponPolicy = coupon.getCouponPolicy();
            return Response.builder()
                    .id(coupon.getId())
                    .userId(coupon.getUserId())
                    .couponCode(coupon.getCouponCode())
                    .discountType(couponPolicy.getDiscountType())
                    .discountValue(couponPolicy.getDiscountValue())
                    .minimumOrderAmount(couponPolicy.getMinimumOrderAmount())
                    .maximumDiscountAmount(couponPolicy.getMaximumDiscountAmount())
                    .validFrom(couponPolicy.getStartTime())
                    .validUntil(couponPolicy.getEndTime())
                    .status(coupon.getStatus())
                    .usedAt(coupon.getUsedAt())
                    .build();
        }
    }

    public record UseRequest(
            Long orderId
    ) {
    }
}

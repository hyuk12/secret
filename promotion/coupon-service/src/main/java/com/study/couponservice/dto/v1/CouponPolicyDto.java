package com.study.couponservice.dto.v1;

import com.study.couponservice.domain.CouponPolicy;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

public record CouponPolicyDto() {
    public static record CreateRequest(
            @NotBlank(message = "쿠폰 정책 이름은 필수입니다.")
            String name,

            String description,

            @NotNull(message = "할인 타입은 필수 입니다.")
            CouponPolicy.DiscountType discountType,

            @NotNull(message = "할인 값은 필수입니다.")
            @Min(value = 1, message = "할인 값은 1 이상이어야 합니다.")
            Integer discountValue,

            @NotNull(message = "최소 주문 금액은 필수입니다.")
            @Min(value = 0, message = "최소 주문 금액은 0 이상이어야 합니다.")
            Integer minimumOrderAmount,

            @NotNull(message = "최대 할인 금액은 필수입니다.")
            @Min(value = 1, message = "최대 할인 금액은 1 이상이어야 합니다.")
            Integer maximumDiscountAmount,

            @NotNull(message = "총 수량은 필수입니다.")
            @Min(value = 1, message = "총 수량은 1 이상이어야 합니다.")
            Integer totalQuantity,

            @NotNull(message = "시작 시간은 필수입니다.")
            LocalDateTime startTime,

            @NotNull(message = "종료 시간은 필수입니다.")
            LocalDateTime endTime
    ){
        public CouponPolicy toEntity() {
            return CouponPolicy.builder()
                    .name(name)
                    .description(description)
                    .discountType(discountType)
                    .discountValue(discountValue)
                    .minimumOrderAmount(minimumOrderAmount)
                    .maximumDiscountAmount(maximumDiscountAmount)
                    .totalQuantity(totalQuantity)
                    .startTime(startTime)
                    .endTime(endTime)
                    .build();
        }
    }

    @Builder
    public static record Response(
            Long id,
            String name,
            String description,
            CouponPolicy.DiscountType discountType,
            Integer discountValue,
            Integer minimumOrderAmount,
            Integer maximumDiscountAmount,
            Integer totalQuantity,
            Integer IssuedQuantity,
            LocalDateTime startTime,
            LocalDateTime endTime,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static Response from(CouponPolicy couponPolicy) {
            return Response.builder()
                    .id(couponPolicy.getId())
                    .name(couponPolicy.getName())
                    .description(couponPolicy.getDescription())
                    .discountType(couponPolicy.getDiscountType())
                    .discountValue(couponPolicy.getDiscountValue())
                    .minimumOrderAmount(couponPolicy.getMinimumOrderAmount())
                    .maximumDiscountAmount(couponPolicy.getMaximumDiscountAmount())
                    .totalQuantity(couponPolicy.getTotalQuantity())
                    .startTime(couponPolicy.getStartTime())
                    .endTime(couponPolicy.getEndTime())
                    .createdAt(couponPolicy.getCreatedAt())
                    .updatedAt(couponPolicy.getUpdatedAt())
                    .build();
        }
    }
}

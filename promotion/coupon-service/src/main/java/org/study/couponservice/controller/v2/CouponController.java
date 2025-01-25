package org.study.couponservice.controller.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.couponservice.dto.v2.CouponDto;
import org.study.couponservice.service.v2.CouponService;

@RestController("couponControllerV2")
@RequestMapping("/api/v2/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/issue")
    public ResponseEntity<CouponDto.CouponResponse> issueCoupon(
            @RequestBody CouponDto.CouponIssueRequest request
    ) {
        return ResponseEntity.ok(couponService.issueCoupon(request));
    }

    @PostMapping("/{couponId}/use")
    public ResponseEntity<CouponDto.CouponResponse> useCoupon(
            @PathVariable Long couponId,
            @RequestBody CouponDto.CouponUseRequest request
    ) {
        return ResponseEntity.ok(couponService.useCoupon(couponId, request.orderId()));
    }

    @PostMapping("/{couponId}/cancel")
    public ResponseEntity<CouponDto.CouponResponse> cancelCoupon(@PathVariable Long couponId) {
        return ResponseEntity.ok(couponService.cancelCoupon(couponId));
    }
}

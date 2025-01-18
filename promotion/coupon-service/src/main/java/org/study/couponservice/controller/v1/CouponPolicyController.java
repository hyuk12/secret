package org.study.couponservice.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.couponservice.dto.CouponPolicyDto;
import org.study.couponservice.service.v1.CouponPolicyService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/coupon-policies")
@RequiredArgsConstructor
public class CouponPolicyController {
    private final CouponPolicyService couponPolicyService;

    @PostMapping
    public ResponseEntity<CouponPolicyDto.Response> createCouponPolicy(
            @RequestBody CouponPolicyDto.CreateRequest request
    ) {
        return ResponseEntity.ok()
                .body(CouponPolicyDto.Response.from(couponPolicyService.createCouponPolicy(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponPolicyDto.Response> getCouponPolicy(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                CouponPolicyDto.Response.from(couponPolicyService.getCouponPolicy(id))
        );
    }

    @GetMapping
    public ResponseEntity<List<CouponPolicyDto.Response>> getAllCouponPolicy() {
        return ResponseEntity.ok(
                couponPolicyService.getCouponPolicies().stream()
                        .map(CouponPolicyDto.Response::from)
                        .toList()
        );
    }
}

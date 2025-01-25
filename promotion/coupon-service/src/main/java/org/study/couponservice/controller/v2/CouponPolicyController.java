package org.study.couponservice.controller.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.couponservice.dto.v2.CouponPolicyDto;
import org.study.couponservice.service.v2.CouponPolicyService;

import java.util.List;

@RestController("couponPolicyControllerV2")
@RequestMapping("/api/v2/coupon-policies")
@RequiredArgsConstructor
public class CouponPolicyController {
    private final CouponPolicyService couponPolicyService;

    @PostMapping
    public ResponseEntity<CouponPolicyDto.Response> createCouponPolicy(
            @RequestBody CouponPolicyDto.CreateRequest request
    ) throws JsonProcessingException {
       return ResponseEntity.ok()
               .body(CouponPolicyDto.Response.from(couponPolicyService.createCouponPolicy(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponPolicyDto.Response> getCouponPolicy(@PathVariable Long id) {
        return ResponseEntity.ok(CouponPolicyDto.Response.from(couponPolicyService.getCouponPolicy(id)));
    }

    @GetMapping
    public ResponseEntity<List<CouponPolicyDto.Response>> getAllCouponPolicies() {
        return ResponseEntity.ok(couponPolicyService.getAllCouponPolicies()
                .stream()
                .map(CouponPolicyDto.Response::from)
                .toList()
        );
    }
}

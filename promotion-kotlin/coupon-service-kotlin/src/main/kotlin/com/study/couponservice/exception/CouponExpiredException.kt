package com.study.couponservice.exception

class CouponExpiredException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(couponId: Long) : super("만료된 쿠폰입니다: $couponId")
}

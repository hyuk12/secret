package com.study.couponservice.exception

class CouponAlreadyUsedException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(couponId: Long) : super("이미 사용된 쿠폰입니다: $couponId")
}

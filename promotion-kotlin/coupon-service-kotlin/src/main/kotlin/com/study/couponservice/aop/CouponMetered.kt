package com.study.couponservice.aop

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CouponMetered(val version: String = "v1")

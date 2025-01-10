package com.study.couponservice.aop

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component

@Aspect
@Component
class CouponMetricsAspect(private val registry: MeterRegistry) {

    @Around("@annotation(CouponMetered)")
    @Throws(Throwable::class)
    fun measureCouponOperation(joinPoint: ProceedingJoinPoint): Any? {
        val sample = Timer.start()
        val version = extractVersion(joinPoint)
        val operation = extractOperation(joinPoint)

        return try {
            val result = joinPoint.proceed()

            // 쿠폰 발급 성공 메트릭
            Counter.builder("coupon.operation.success")
                .tag("version", version)
                .tag("operation", operation)
                .register(registry)
                .increment()

            sample.stop(Timer.builder("coupon.operation.duration")
                .tag("version", version)
                .tag("operation", operation)
                .register(registry))

            result
        } catch (e: Exception) {
            // 쿠폰 발급 실패 메트릭
            Counter.builder("coupon.operation.failure")
                .tag("version", version)
                .tag("operation", operation)
                .tag("error", e::class.simpleName ?: "UnknownError")
                .register(registry)
                .increment()
            throw e
        }
    }

    private fun extractVersion(joinPoint: ProceedingJoinPoint): String {
        val method = (joinPoint.signature as MethodSignature).method
        return method.getAnnotation(CouponMetered::class.java).version
    }

    private fun extractOperation(joinPoint: ProceedingJoinPoint): String {
        return joinPoint.signature.name
    }
}

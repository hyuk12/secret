package com.study.couponservice.aop

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import kotlin.jvm.Throws
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Aspect
@Component
class MetricsAspect(
    private val registry: MeterRegistry,
) {

    @Around("@annotation(Metered)")
    @Throws(Throwable::class)
    fun measureExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val sample = Timer.start()
        val methodName = joinPoint.signature.name
        val className = joinPoint.signature.declaringType.simpleName

        try {
            val result = joinPoint.proceed()

            // 성공 메트릭 기록
            Counter.builder("method.invocation.success")
                .tag("class", className)
                .tag("method", methodName)
                .register(registry)
                .increment()

            sample.stop(Timer.builder("method.execution.time")
                .tag("class", className)
                .tag("method", methodName)
                .register(registry))

            return result
        } catch (e: Exception) {
            Counter.builder("method.invocation.failure")
                .tag("class", className)
                .tag("method", methodName)
                .tag("exception", e::class.simpleName ?: "Unknown Error")
                .register(registry)
                .increment();
            throw e;
        }
    }
}

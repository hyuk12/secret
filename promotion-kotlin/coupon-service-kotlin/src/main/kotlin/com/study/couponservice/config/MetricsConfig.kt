package com.study.couponservice.config

import com.study.couponservice.aop.CouponMetricsAspect
import com.study.couponservice.aop.MetricsAspect
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy

@Configuration
@EnableAspectJAutoProxy
class MetricsConfig {
    @Bean
    fun metricsAspect(registry: MeterRegistry): MetricsAspect {
        return MetricsAspect(registry)
    }

    @Bean
    fun couponMetricsAspect(registry: MeterRegistry): CouponMetricsAspect {
        return CouponMetricsAspect(registry)
    }
}

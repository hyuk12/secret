package org.study.couponservice.service.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.couponservice.config.UserIdInterceptor;
import org.study.couponservice.domain.Coupon;
import org.study.couponservice.domain.CouponPolicy;
import org.study.couponservice.dto.v2.CouponDto;
import org.study.couponservice.exception.CouponIssueException;
import org.study.couponservice.repository.CouponRepository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponRedisService {
    private final RedissonClient redissonClient;
    private final CouponRepository couponRepository;
    private final CouponPolicyService couponPolicyService;

    private static final String COUPON_QUANTITY_KEY = "coupon:quantity:";
    private static final String COUPON_LOCK_KEY = "coupon:lock:";
    private static final long LOCK_WAIT_TIME = 3; // 3초 락이 걸리는 시간
    private static final long LOCK_LEASE_TIME = 5;

    @Transactional
    public Coupon issueCoupon(CouponDto.CouponIssueRequest request) {
        String quantityKey = COUPON_QUANTITY_KEY + request.couponPolicyId();
        String lockKey = COUPON_LOCK_KEY + request.couponPolicyId();
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new CouponIssueException("쿠폰 발급 요청이 많아서 처리할 수 없습니다, 잠시 후 다시 시도해주세요.");
            }

            CouponPolicy couponPolicy = couponPolicyService.getCouponPolicy(request.couponPolicyId());

            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(couponPolicy.getStartTime()) || now.isAfter(couponPolicy.getEndTime())) {
                throw new IllegalStateException("쿠폰 발급 기간이 아닙니다.");
            }

            // 수량 체크 및 감소
            RAtomicLong atomicLong = redissonClient.getAtomicLong(quantityKey);
            long remainingQuantity = atomicLong.decrementAndGet();

            if (remainingQuantity < 0) {
                atomicLong.incrementAndGet();
                throw new CouponIssueException("쿠폰이 모두 소진되었습니다.");
            }

            return couponRepository.save(Coupon.builder()
                            .couponPolicy(couponPolicy)
                            .userId(UserIdInterceptor.getCurrentUserId())
                            .couponCode(generateCouponCode())
                    .build());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CouponIssueException("쿠폰 발급 중 오류가 발생했습니다.");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private String generateCouponCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}

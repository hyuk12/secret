package com.study.couponservice.repository;

import com.study.couponservice.domain.CouponPolicy;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {

    // 비관적 락 쓰기잠금 동시성 문제 해결을 위한 경우 사용이 됨
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cp FROM CouponPolicy cp WHERE cp.id = :id")
    Optional<CouponPolicy> findByIdWithLock(Long id);
}

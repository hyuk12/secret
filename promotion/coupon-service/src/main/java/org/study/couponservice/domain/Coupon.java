package org.study.couponservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.study.couponservice.exception.CouponAlreadyUsedException;
import org.study.couponservice.exception.CouponExpiredException;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id")
    private CouponPolicy couponPolicy;

    private Long userId;
    private String couponCode;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Long orderId;
    private LocalDateTime usedAt;
    private LocalDateTime createdAt;

    @Builder
    public Coupon(Long id, CouponPolicy couponPolicy, Long userId, String couponCode) {
        this.id = id;
        this.couponPolicy = couponPolicy;
        this.userId = userId;
        this.couponCode = couponCode;
        this.status = Status.AVAILABLE;
    }

    public enum Status {
        AVAILABLE,
        USED,
        EXPIRED,
        CANCELED
    }

    // 비지니스 로직
    public void use(Long orderId) {
        if (status == Status.USED) {
            throw new CouponAlreadyUsedException("이미 사용된 쿠폰입니다.");
        }

        if (isExpired()) {
            throw new CouponExpiredException("만료된 쿠폰입니다.");
        }

        this.status = Status.USED;
        this.orderId = orderId;
        this.usedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (status != Status.USED) {
            throw new IllegalStateException("사용되지 않은 쿠폰 입니다.");
        }

        this.status = Status.CANCELED;
        this.orderId = null;
        this.usedAt = null;
    }

    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(couponPolicy.getStartTime()) || now.isAfter(couponPolicy.getEndTime());
    }
}

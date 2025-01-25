package org.study.couponservice.service.v1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.couponservice.config.UserIdInterceptor;
import org.study.couponservice.domain.Coupon;
import org.study.couponservice.domain.CouponPolicy;
import org.study.couponservice.dto.v1.CouponDto;
import org.study.couponservice.repository.CouponPolicyRepository;
import org.study.couponservice.repository.CouponRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {
    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    private CouponPolicy couponPolicy;
    private Coupon coupon;
    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_COUPON_ID = 1L;
    private static final Long TEST_ORDER_ID = 1L;

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicy.builder()
                .id(1L)
                .name("테스트 쿠폰")
                .discountType(CouponPolicy.DiscountType.FIXED_AMOUNT)
                .discountValue(1000)
                .minimumOrderAmount(10000)
                .maximumDiscountAmount(1000)
                .totalQuantity(100)
                .startTime(LocalDateTime.now().minusDays(1))
                .endTime(LocalDateTime.now().plusDays(1))
                .build();

        coupon = Coupon.builder()
                .id(TEST_COUPON_ID)
                .userId(TEST_USER_ID)
                .couponPolicy(couponPolicy)
                .couponCode("TEST123")
                .build();
    }

    @Test
    @DisplayName("쿠폰 발급 성공")
    void issueCoupon_Success() {
        // given
        CouponDto.IssueRequest request = CouponDto.IssueRequest.builder()
                .couponPolicyId(1L)
                .build();

        when(couponPolicyRepository.findByIdWithLock(any())).thenReturn(Optional.of(couponPolicy));
        when(couponRepository.countByCouponPolicyId(any())).thenReturn(0L);
        when(couponRepository.save(any())).thenReturn(coupon);

        try (MockedStatic<UserIdInterceptor> mockedStatic = mockStatic(UserIdInterceptor.class)){
            mockedStatic.when(UserIdInterceptor::getCurrentUserId).thenReturn(TEST_USER_ID);

            // when
            CouponDto.Response response = CouponDto.Response.from(couponService.issueCoupon(request));

            // then
            assertThat(response.id()).isEqualTo(TEST_COUPON_ID);
            assertThat(response.userId()).isEqualTo(TEST_USER_ID);
            verify(couponRepository).save(any());
        }
    }

    @Test
    @DisplayName("쿠폰 사용 성공")
    void useCoupon_Success() {
        // given
        when(couponRepository.findByIdAndUserId(TEST_COUPON_ID, TEST_USER_ID))
                .thenReturn(Optional.of(coupon));

        try (MockedStatic<UserIdInterceptor> mockedStatic = mockStatic(UserIdInterceptor.class)) {
            mockedStatic.when(UserIdInterceptor::getCurrentUserId).thenReturn(TEST_USER_ID);

            // when
            CouponDto.Response response = CouponDto.Response.from(couponService.useCoupon(TEST_COUPON_ID, TEST_ORDER_ID));

            // then
            assertThat(response.id()).isEqualTo(TEST_COUPON_ID);
            assertThat(response.userId()).isEqualTo(TEST_USER_ID);
            assertThat(response.status()).isEqualTo(Coupon.Status.USED);
        }
    }

    @Test
    @DisplayName("쿠폰 사용 실패")
    void useCoupon_Fail_NotFoundOrUnauthorized() {

    }

    @Test
    @DisplayName("쿠폰 취소 성공")
    void cancelCoupon_Success() {

    }

    @Test
    @DisplayName("쿠폰 취소 실패 - 쿠폰이 존재하지 않거나 권한이 없음")
    void cancelCoupon_Fail_NotFoundOrUnauthorized() {

    }


}
package org.study.couponservice.exception;

public class CouponAlreadyUsedException extends RuntimeException{
    public CouponAlreadyUsedException(String message) {
        super(message);
    }
}

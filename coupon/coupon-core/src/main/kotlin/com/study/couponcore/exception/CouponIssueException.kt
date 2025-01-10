package com.study.couponcore.exception

import java.lang.RuntimeException

class CouponIssueException(errorCode: ErrorCode, message: String) : RuntimeException(message) {
}
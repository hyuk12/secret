package com.study.couponservice.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class UserIdInterceptor : HandlerInterceptor {

    companion object {
        private const val USER_ID_HEADER = "X-USER-ID"
        private val currentUserId: ThreadLocal<Long> = ThreadLocal()

        fun getCurrentUserId(): Long {
            return currentUserId.get() ?: throw IllegalStateException("User ID not found in current context")
        }
    }

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val userIdStr = request.getHeader(USER_ID_HEADER)
            ?: throw IllegalStateException("X-USER-ID header is required")
        currentUserId.set(userIdStr.toLongOrNull() ?: throw IllegalStateException("Invalid X-USER-ID format"))
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        currentUserId.remove()
    }
}

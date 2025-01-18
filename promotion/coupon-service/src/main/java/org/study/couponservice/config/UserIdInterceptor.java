package org.study.couponservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class UserIdInterceptor implements HandlerInterceptor {
    private static final String USER_ID_HEADER = "X-USER-ID";
    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIdStr = request.getHeader(USER_ID_HEADER);
        log.info("Received X-USER-ID: {}", userIdStr);

        if (userIdStr == null || userIdStr.isEmpty()) {
            throw new IllegalStateException("요청 헤더에 X-USER-ID가 누락되었습니다");
        }

        try {
            currentUserId.set(Long.parseLong(userIdStr));
            return true;
        } catch (NumberFormatException e) {
            throw new IllegalStateException("요청 헤더 X-USER-ID의 값이 올바른 숫자 형식이 아닙니다.");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        currentUserId.remove();
    }

    public static Long getCurrentUserId() {
        Long userId = currentUserId.get();
        if (userId == null) {
            throw new IllegalStateException("User ID not found in current context");
        }
        return userId;
    }
}

package org.ziranziyuanting.authcenter.config.log;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LogInterceptor implements HandlerInterceptor {
    
    @SuppressWarnings("null")
    @Override
    public boolean preHandle(HttpServletRequest request, 
                           HttpServletResponse response, Object handler) {
        // 从请求头或token中获取用户信息
        // String userId = getUserIdFromRequest(request);
        String requestId = UUID.randomUUID().toString();
        
        MDC.put("userId", "yanglu");
        MDC.put("requestId", requestId);
        MDC.put("clientIp", request.getRemoteAddr());
        MDC.put("userAgent", request.getHeader("User-Agent"));
        
        return true;
    }
    
    @SuppressWarnings("null")
    @Override
    public void afterCompletion(HttpServletRequest request,
                              HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清理MDC
        MDC.clear();
    }
}

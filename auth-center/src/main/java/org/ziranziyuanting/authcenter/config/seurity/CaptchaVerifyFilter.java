package org.ziranziyuanting.authcenter.config.seurity;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录验证码校验过滤器。
 * 仅拦截 POST /login 请求，从 Redis 中取出之前生成的验证码（key 为 "captcha:" + sessionId）进行比对，
 * 比对后无论成败都立即删除该 key（一次性使用，防止重放）。
 * 校验失败时设置 session 标记并重定向回登录页，由 LoginController 展示"验证码错误"提示。
 *
 * 注意：本类不要加 @Component/@Bean 注解，否则会被 Spring Boot 自动注册为全局 Servlet Filter，
 * 导致脱离安全过滤器链执行。应在 SecurityConfig 中通过 new CaptchaVerifyFilter(redisTemplate) 显式挂载。
 */
public class CaptchaVerifyFilter extends OncePerRequestFilter {

    /** Redis 中验证码 key 的前缀，完整 key 为 "captcha:" + sessionId */
    public static final String CAPTCHA_KEY_PREFIX = "captcha:";
    /** 验证码过期时间（分钟），集中在常量便于统一调整 */
    public static final long CAPTCHA_TTL_MINUTES = 5L;
    /** session 中"验证码错误"提示标记，LoginController 读取后移除 */
    public static final String SESSION_CAPTCHA_ERROR = "captchaError";
    /** 验证码失败时暂存用户名的 session key，重定向回登录页后由 LoginController 回填（一次性） */
    public static final String SESSION_LOGIN_USERNAME = "loginUsername";
    /** 验证码失败时暂存密码的 session key，重定向回登录页后由 LoginController 回填（一次性） */
    public static final String SESSION_LOGIN_PASSWORD = "loginPassword";
    /** 登录认证的 POST 地址，与 SecurityConfig 的 loginProcessingUrl 一致 */
    private static final String LOGIN_PROCESSING_URL = "/login";
    /** 登录页地址，与 SecurityConfig 的 loginPage 一致 */
    private static final String LOGIN_PAGE_URL = "/custom-login";

    private final StringRedisTemplate redisTemplate;

    public CaptchaVerifyFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 仅校验登录 POST 请求，其余请求直接放行
        if (!"POST".equalsIgnoreCase(request.getMethod())
                || !LOGIN_PROCESSING_URL.equals(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        boolean valid;
        if (session == null) {
            // 没有 session 说明从未生成过验证码，直接判定失败，防止脚本绕过
            valid = false;
        } else {
            String key = CAPTCHA_KEY_PREFIX + session.getId();
            Object expected = redisTemplate.opsForValue().get(key);
            String input = request.getParameter("captcha");
            valid = expected != null && input != null
                    && expected.toString().equalsIgnoreCase(input.trim());
            // 无论验证码对错都立即删除，一次性使用，防止重放攻击
            redisTemplate.delete(key);
        }

        if (valid) {
            filterChain.doFilter(request, response);
            return;
        }

        // 校验失败：暂存用户名密码以便登录页回填，记录错误标记并重定向回登录页
        HttpSession sessionToUse = request.getSession();
        sessionToUse.setAttribute(SESSION_CAPTCHA_ERROR, Boolean.TRUE);
        sessionToUse.setAttribute(SESSION_LOGIN_USERNAME, request.getParameter("username"));
        sessionToUse.setAttribute(SESSION_LOGIN_PASSWORD, request.getParameter("password"));
        response.sendRedirect(request.getContextPath() + LOGIN_PAGE_URL);
    }
}

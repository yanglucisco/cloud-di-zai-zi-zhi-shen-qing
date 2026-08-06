package org.ziranziyuanting.authcenter.controller;

import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.web.WebAttributes;
import org.ziranziyuanting.authcenter.config.seurity.CaptchaVerifyFilter;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @GetMapping("/custom-login") // 这个路径将在安全配置中指定为登录页
    public String loginPage(HttpSession session, Model model) {
        // 优先处理验证码错误标记（一次性展示，读取后立即移除）
        Object captchaError = session.getAttribute(CaptchaVerifyFilter.SESSION_CAPTCHA_ERROR);
        if (captchaError != null) {
            session.removeAttribute(CaptchaVerifyFilter.SESSION_CAPTCHA_ERROR);
            // 回填验证码失败时用户填写的用户名密码，只清空验证码输入框
            Object savedUsername = session.getAttribute(CaptchaVerifyFilter.SESSION_LOGIN_USERNAME);
            Object savedPassword = session.getAttribute(CaptchaVerifyFilter.SESSION_LOGIN_PASSWORD);
            session.removeAttribute(CaptchaVerifyFilter.SESSION_LOGIN_USERNAME);
            session.removeAttribute(CaptchaVerifyFilter.SESSION_LOGIN_PASSWORD);
            model.addAttribute("error", "验证码错误");
            model.addAttribute("username", savedUsername == null ? "" : savedUsername.toString());
            model.addAttribute("password", savedPassword == null ? "" : savedPassword.toString());
            return "login";
        }
        // 从 session 中取出登录失败时的异常，将提示信息带到页面
        Object exception = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (exception instanceof DisabledException) {
            model.addAttribute("error", "用户已禁用");
        } else if (exception instanceof Exception) {
            model.addAttribute("error", "登录信息填写错误");
        }
        return "login"; // 返回模板名称（对应login.html）
    }
}

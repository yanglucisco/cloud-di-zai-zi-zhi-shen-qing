package org.ziranziyuanting.authcenter.controller;

import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.web.WebAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    @GetMapping("/custom-login") // 这个路径将在安全配置中指定为登录页
    public String loginPage(HttpSession session, Model model) {
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

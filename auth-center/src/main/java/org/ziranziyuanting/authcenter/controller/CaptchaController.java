package org.ziranziyuanting.authcenter.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.ziranziyuanting.authcenter.config.seurity.CaptchaVerifyFilter;

import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 登录页图形验证码生成接口。
 * GET /captcha 生成 4 位验证码图片（数字 + 小写字母混合），并将验证码存入 Redis，
 * key 为 "captcha:" + sessionId，TTL 5 分钟，与 session 解耦、独立过期。
 */
@Controller
public class CaptchaController {

    /** 小写字母字符集（需求要求字母全部小写） */
    private static final char[] LETTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** 数字字符集 */
    private static final char[] DIGITS = "0123456789".toCharArray();

    private static final int WIDTH = 130;
    private static final int HEIGHT = 40;
    private static final int CODE_LENGTH = 4;

    private final SecureRandom random = new SecureRandom();
    private final StringRedisTemplate redisTemplate;

    public CaptchaController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 生成数字与字母混合的 4 位验证码
        String code = generateCode();

        // 存入 Redis：key 带 sessionId，TTL 独立于 session
        HttpSession session = request.getSession();
        redisTemplate.opsForValue().set(
                CaptchaVerifyFilter.CAPTCHA_KEY_PREFIX + session.getId(),
                code,
                CaptchaVerifyFilter.CAPTCHA_TTL_MINUTES,
                TimeUnit.MINUTES);

        // 响应图片并禁止缓存（登录失败重定向回本页时能拿到新验证码）
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        ImageIO.write(drawImage(code), "png", response.getOutputStream());
    }

    /**
     * 生成 4 位验证码，保证数字与字母混合：
     * 数字数量取 1~3 个（随机），其余位置放小写字母，从而至少 1 个数字、至少 1 个字母。
     */
    private String generateCode() {
        char[] result = new char[CODE_LENGTH];
        boolean[] isDigit = new boolean[CODE_LENGTH];
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < CODE_LENGTH; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions, random);

        int digitCount = 1 + random.nextInt(3); // 1 ~ 3 个数字
        for (int i = 0; i < digitCount; i++) {
            isDigit[positions.get(i)] = true;
        }
        for (int i = 0; i < CODE_LENGTH; i++) {
            result[i] = isDigit[i]
                    ? DIGITS[random.nextInt(DIGITS.length)]
                    : LETTERS[random.nextInt(LETTERS.length)];
        }
        return new String(result);
    }

    /** 绘制验证码图片：白底、干扰线、噪点、逐字符轻微旋转 */
    private BufferedImage drawImage(String code) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 白底
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // 5 条随机干扰线
        for (int i = 0; i < 5; i++) {
            g2d.setColor(randomColor(180, 230));
            g2d.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT),
                    random.nextInt(WIDTH), random.nextInt(HEIGHT));
        }

        // 60 个随机噪点
        g2d.setColor(randomColor(150, 220));
        for (int i = 0; i < 60; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            g2d.drawRect(x, y, 1, 1);
        }

        // 逐字符绘制，随机深浅色 + 轻微旋转
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 28);
        for (int i = 0; i < code.length(); i++) {
            String ch = String.valueOf(code.charAt(i));
            int x = 12 + i * 28;
            int y = 28;
            Graphics2D sub = (Graphics2D) g2d.create();
            sub.setFont(font);
            sub.setColor(randomColor(30, 150));
            sub.rotate((random.nextDouble() - 0.5) * 0.5, x, y); // ±0.25 rad
            sub.drawString(ch, x, y);
            sub.dispose();
        }

        g2d.dispose();
        return image;
    }

    /** 生成 RGB 分量在 [min, max] 范围内的随机颜色 */
    private Color randomColor(int min, int max) {
        int range = max - min;
        return new Color(min + random.nextInt(range),
                min + random.nextInt(range),
                min + random.nextInt(range));
    }
}

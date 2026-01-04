package org.ziranziyuanting.rolemanage.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {
    private static String idForEncode = "bcrypt";
    private static Map<String, PasswordEncoder> encoders = new HashMap<>();
    static {
        encoders.put(idForEncode, new BCryptPasswordEncoder());
    }

    public static PasswordEncoder BCryptPasswordEncoder() {
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    public static PasswordEncoder BCryptPasswordEncoder(int factor) {
        return new BCryptPasswordEncoder(factor);
    }
    /**
     * 获取3个不重复的随机英文字母
     */
    private static String getRandomUniqueLetters() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        boolean[] used = new boolean[26]; // 记录字母是否已使用
        
        while (result.length() < 3) {
            int index = random.nextInt(26);
            if (!used[index]) {
                char randomChar = (char) ('a' + index);
                result.append(randomChar);
                used[index] = true;
            }
        }
        
        return result.toString();
    }
    /**
     * 生成包含3个数字和2个特殊字符的随机字符串
     * 特殊字符从 !@#$%^&* 中选择
     */
    private static String generateRandomCode() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        
        // 特殊字符集合
        char[] specialChars = {'!', '@', '#', '$', '%', '^', '&', '*'};
        
        // 生成3个随机数字 (0-9)
        for (int i = 0; i < 3; i++) {
            int randomDigit = random.nextInt(10); // 生成0-9的随机数
            result.append(randomDigit);
        }
        
        // 生成2个随机特殊字符
        for (int i = 0; i < 2; i++) {
            int randomIndex = random.nextInt(specialChars.length);
            result.append(specialChars[randomIndex]);
        }
        
        return result.toString();
    }
    public static String generatePassword(String str){
        return BCryptPasswordEncoder().encode(str);
    }
    public static String generateRandomString(String account){
        return account + getRandomUniqueLetters() + generateRandomCode();
    }
}

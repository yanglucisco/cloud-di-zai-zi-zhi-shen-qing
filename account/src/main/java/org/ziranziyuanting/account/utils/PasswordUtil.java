package org.ziranziyuanting.account.utils;

import java.util.HashMap;
import java.util.Map;

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
}

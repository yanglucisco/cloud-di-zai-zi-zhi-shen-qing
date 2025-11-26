package org.ziranziyuanting.authcenter.config.seurity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // @Autowired
    // private UserService userService; // 例如从数据库中获取用户

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<String> adminRoles = new ArrayList<>();
        adminRoles.add("ROLE_admin");
        adminRoles.add("ROLE_normal");
        if ("admin".equals(username)) {
            return new CustomUserDetails(
                    1L,
                    "admin",
                    "{noop}adminpwd",
                    "",
                    adminRoles,
                    true, true, true, true 
            );
        }
        List<String> yangluRoles = new ArrayList<>();
        yangluRoles.add("ROLE_normal");
        return new CustomUserDetails(
                1L,
                "yanglu",
                "{noop}yanglupwd",
                "",
                yangluRoles,
                true, true, true, true 
        );
    }
}

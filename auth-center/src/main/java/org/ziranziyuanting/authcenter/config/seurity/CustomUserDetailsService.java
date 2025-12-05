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
        List<String> permissions = List.of(
            "catalog.read", "catalog.edit", "catalog.delete",
            "employee.read", "employee.edit", "employee.delete",
            "work.read", "work.edit", "work.delete",
            "order.read", "order.edit", "order.delete",
            "abc.read", "abc.edit", "abc.delete",
            "catalog1.read", "catalog1.edit", "catalog1.delete",
            "catalog2.read", "catalog2.edit", "catalog2.delete",
            "catalog3.read", "catalog3.edit", "catalog3.delete",
            "catalog4.read", "catalog4.edit", "catalog4.delete"
        );
        adminRoles.addAll(permissions);
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
        List<String> permissions1 = List.of("catalog.read");
        yangluRoles.addAll(permissions1);
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

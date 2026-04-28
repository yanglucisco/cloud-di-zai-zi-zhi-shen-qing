package org.ziranziyuanting.authcenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/my")
public class CustomLoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Handles the login submission.
     * Note: In a standard Spring Security setup, you usually let the Filter Chain handle this.
     * However, if you want to manually invoke authentication using the configured providers:
     */
    @PostMapping("/mylogin")
    public String performLogin(@RequestParam String username,
                             @RequestParam String password) throws IOException {
        try {
            // Create an authentication token
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(username, password);

            // Authenticate using the AuthenticationManager (which uses your CustomUserDetailsService and PasswordEncoder)
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Set the authentication in the context
            SecurityContextHolder.getContext().setAuthentication(authentication);

           return "成功";

        } catch (Exception e) {
            return "失败";
        }
    }
}
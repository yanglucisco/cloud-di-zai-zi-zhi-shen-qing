package org.ziranziyuanting.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    private List<String> permitAll = new ArrayList<>();
    private List<String> authenticated = new ArrayList<>();
    private List<String> adminPaths = new ArrayList<>();
    
    // Getters and Setters
    public List<String> getPermitAll() {
        return permitAll;
    }
    
    public void setPermitAll(List<String> permitAll) {
        this.permitAll = permitAll;
    }
    
    public List<String> getAuthenticated() {
        return authenticated;
    }
    
    public void setAuthenticated(List<String> authenticated) {
        this.authenticated = authenticated;
    }
    
    public List<String> getAdminPaths() {
        return adminPaths;
    }
    
    public void setAdminPaths(List<String> adminPaths) {
        this.adminPaths = adminPaths;
    }
}

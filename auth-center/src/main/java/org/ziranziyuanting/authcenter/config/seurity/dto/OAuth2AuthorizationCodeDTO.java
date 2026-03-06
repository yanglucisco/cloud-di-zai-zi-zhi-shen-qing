package org.ziranziyuanting.authcenter.config.seurity.dto;


import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;

import lombok.Data;

@Data
public class OAuth2AuthorizationCodeDTO {
    private String tokenValue;
    private java.time.Instant issuedAt;
    private java.time.Instant expiresAt;
    public OAuth2AuthorizationCode to(){
        OAuth2AuthorizationCode r = new OAuth2AuthorizationCode(getTokenValue(), getIssuedAt(), getExpiresAt());
        return r;
    }
    public static OAuth2AuthorizationCodeDTO from(OAuth2AuthorizationCode code){
        OAuth2AuthorizationCodeDTO r = new OAuth2AuthorizationCodeDTO();
        r.setExpiresAt(code.getExpiresAt());
        r.setIssuedAt(code.getIssuedAt());
        r.setTokenValue(code.getTokenValue());
        return r;
    }
}

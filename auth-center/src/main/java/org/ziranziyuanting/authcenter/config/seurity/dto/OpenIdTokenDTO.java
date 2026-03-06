package org.ziranziyuanting.authcenter.config.seurity.dto;

import java.util.Map;

import org.springframework.security.oauth2.core.oidc.OidcIdToken;

import lombok.Data;

@Data
public class OpenIdTokenDTO {
    private String tokenValue;
    private java.time.Instant issuedAt;
    private java.time.Instant expiresAt;
    private Map<String, Object> claims;
    public static OpenIdTokenDTO from(OidcIdToken token){
        OpenIdTokenDTO r = new OpenIdTokenDTO();
        r.setExpiresAt(token.getExpiresAt());
        r.setIssuedAt(token.getIssuedAt());
        r.setClaims(token.getClaims());
        r.setTokenValue(token.getTokenValue());
        return r;
    }
    public OidcIdToken to(){
        OidcIdToken r = new OidcIdToken(tokenValue, issuedAt, expiresAt, claims);
        return r;
    }
}

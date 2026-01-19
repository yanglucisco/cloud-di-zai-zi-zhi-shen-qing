package org.ziranziyuanting.authcenter.config.seurity.dto;

import java.util.Set;

import org.springframework.security.oauth2.core.OAuth2AccessToken;

import lombok.Data;

@Data
public class OAuth2AccessTokenDTO {
    private String tokenValue;
    private java.time.Instant issuedAt;
    private java.time.Instant expiresAt;
    private String tokenType;
    private Set<String> scopes;
    public static OAuth2AccessTokenDTO from(OAuth2AccessToken token){
        OAuth2AccessTokenDTO r = new OAuth2AccessTokenDTO();
        r.setExpiresAt(token.getExpiresAt());
        r.setIssuedAt(token.getIssuedAt());
        r.setScopes(token.getScopes());
        r.setTokenType(token.getTokenType().getValue());
        r.setTokenValue(token.getTokenValue());
        return r;
    }
    public OAuth2AccessToken to(){
        //TokenType tokenType, String tokenValue, Instant issuedAt, Instant expiresAt, Set<String> scopes
        OAuth2AccessToken.TokenType tokenType = OAuth2AccessToken.TokenType.BEARER;
        OAuth2AccessToken r = new OAuth2AccessToken(tokenType, tokenValue, issuedAt, expiresAt, scopes);
        // r.setExpiresAt(token.getExpiresAt());
        // r.setIssuedAt(token.getIssuedAt());
        // r.setScopes(token.getScopes());
        // r.setTokenType(token.getTokenType().getValue());
        // r.setTokenValue(token.getTokenValue());
        return r;
    }
}

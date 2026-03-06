package org.ziranziyuanting.authcenter.config.seurity.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.StringUtils;
import org.ziranziyuanting.authcenter.config.seurity.CustomUserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2AuthorizationDTO implements Serializable {

    private String id;
    private String registeredClientId;
    private String principalName;
    private String authorizationGrantType;
    private Set<String> authorizedScopes;
    private Map<String, Object> attributes; // 存储PKCE等属性
    private String state;

    // Tokens
    // org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode
    // org.springframework.security.oauth2.core.OAuth2AccessToken $TokenType
    private OAuth2AuthorizationCodeDTO authorizationCode;
    // org.springframework.security.oauth2.core.OAuth2AccessToken
    private OAuth2AccessTokenDTO accessToken;
    private OpenIdTokenDTO openIdToken;
    private OAuth2RefreshToken refreshToken;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenAndMetadata {
        private OAuth2Token token;
        private Map<String, Object> metadata;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenDTO implements Serializable {
        private String tokenValue;
        private Instant issuedAt;
        private Instant expiresAt;
        private Map<String, Object> metadata;

        public static TokenDTO from(OAuth2Authorization.Token<?> token) {
            if (token == null)
                return null;

            return new TokenDTO(
                    token.getToken().getTokenValue(),
                    token.getToken().getIssuedAt(),
                    token.getToken().getExpiresAt(),
                    token.getMetadata());
        }

        public TokenAndMetadata toToken() {
            OAuth2Token token = new OAuth2Token() {
                @Override
                public String getTokenValue() {
                    return tokenValue;
                }

                @Override
                public Instant getIssuedAt() {
                    return issuedAt;
                }

                @Override
                public Instant getExpiresAt() {
                    return expiresAt;
                }
            };
            return new TokenAndMetadata(token, metadata);
            // return new OAuth2Authorization.Token<>(token, metadata);
        }
    }

    /**
     * 从 OAuth2Authorization 转换
     */
    public static OAuth2AuthorizationDTO from(OAuth2Authorization authorization) {
        OAuth2AuthorizationDTO dto = new OAuth2AuthorizationDTO();
        dto.setId(authorization.getId());
        dto.setRegisteredClientId(authorization.getRegisteredClientId());
        dto.setPrincipalName(authorization.getPrincipalName());
        dto.setAuthorizationGrantType(
                authorization.getAuthorizationGrantType().getValue());
        dto.setAuthorizedScopes(authorization.getAuthorizedScopes());

        var attributes = new HashMap<>(authorization.getAttributes());
        OAuth2AuthorizationRequest oAuth2AuthorizationRequest = (OAuth2AuthorizationRequest) attributes
                .get("org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest");
        OAuth2AuthorizationRequestDTO oAuth2AuthorizationRequestDTO = OAuth2AuthorizationRequestDTO.from(oAuth2AuthorizationRequest);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) attributes
                .get("java.security.Principal");
        UsernamePasswordAuthenticationTokenDTO usernamePasswordAuthenticationTokenDTO = 
        UsernamePasswordAuthenticationTokenDTO.from(usernamePasswordAuthenticationToken);

        attributes.clear();
        attributes.put("OAuth2AuthorizationRequestDTO", oAuth2AuthorizationRequestDTO);
        attributes.put("Principal", usernamePasswordAuthenticationTokenDTO);
        dto.setAttributes(attributes);

        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
                .getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null) {
            dto.setAuthorizationCode(OAuth2AuthorizationCodeDTO.from(authorizationCode.getToken()));
        }

        // 索引访问令牌
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
        if (accessToken != null) {
            dto.setAccessToken(OAuth2AccessTokenDTO.from(accessToken.getToken()));
        }

        // 索引刷新令牌
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getToken(OAuth2RefreshToken.class);
        if (refreshToken != null) {
            dto.setRefreshToken(refreshToken.getToken());
        }

        // OpenID令牌
        OAuth2Authorization.Token<OidcIdToken> openIdToken = authorization.getToken(OidcIdToken.class);
        if (openIdToken != null) {
            dto.setOpenIdToken(OpenIdTokenDTO.from(openIdToken.getToken()));
        }
        return dto;
    }
    // public static OAuth2Authorization
    // toOAuth2Authorization(OAuth2AuthorizationDTO dto){
    // OAuth2Authorization r = OAuth2Authorization.withRegisteredClient(null)
    // }

    /**
     * 转换为 OAuth2Authorization
     */
    public OAuth2Authorization toOAuth2Authorization(
            RegisteredClientRepository registeredClientRepository) {

        RegisteredClient registeredClient = registeredClientRepository
                .findById(this.registeredClientId);

        if (registeredClient == null) {
            throw new RuntimeException("Registered client not found: " + registeredClientId);
        }

        OAuth2Authorization.Builder builder = OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .id(this.id)
                .principalName(this.principalName)
                .authorizationGrantType(
                        new AuthorizationGrantType(this.authorizationGrantType))
                .authorizedScopes(this.authorizedScopes);

        // 设置属性
        OAuth2AuthorizationRequestDTO oAuth2AuthorizationRequestDTO = (OAuth2AuthorizationRequestDTO)attributes.get("OAuth2AuthorizationRequestDTO");
        UsernamePasswordAuthenticationTokenDTO usernamePasswordAuthenticationTokenDTO = (UsernamePasswordAuthenticationTokenDTO)attributes.get("Principal");
        builder.attribute("org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest", oAuth2AuthorizationRequestDTO.to());
        builder.attribute("java.security.Principal", usernamePasswordAuthenticationTokenDTO.toUsernamePasswordAuthenticationToken());

        // 恢复 State
        if (StringUtils.hasText(this.state)) {
            builder.attribute(OAuth2ParameterNames.STATE, this.state);
        }

        OAuth2Authorization authorization = builder.build();

        // 恢复 Tokens
        if (this.authorizationCode != null) {
            OAuth2AuthorizationCode aOAuth2AuthorizationCode = this.authorizationCode.to();
            authorization = OAuth2Authorization.from(authorization).token(aOAuth2AuthorizationCode).build();
        // OAuth2Authorization.Token<OAuth2Token>
        // TokenAndMetadata token = this.authorizationCode.toToken();
        // authorization = OAuth2Authorization.from(authorization)
        // .token(token.getToken(), metadata -> metadata.putAll(token.getMetadata()))
        // .build();
        }

        if (this.accessToken != null) {
        // OAuth2Authorization.Token<OAuth2Token>
        authorization = OAuth2Authorization.from(authorization).token(this.accessToken.to()).build();
        // TokenAndMetadata token = this.accessToken.toToken();
        // authorization = OAuth2Authorization.from(authorization)
        // .token(token.getToken(), metadata -> metadata.putAll(token.getMetadata()))
        // .build();
        }
        if(this.openIdToken != null){
            authorization = OAuth2Authorization.from(authorization).token(this.openIdToken.to()).build();
        }

        // if (this.refreshToken != null) {
        // // OAuth2Authorization.Token<OAuth2Token>
        // TokenAndMetadata token = this.refreshToken.toToken();
        // authorization = OAuth2Authorization.from(authorization)
        // .token(token.getToken(), metadata -> metadata.putAll(token.getMetadata()))
        // .build();
        // }

        return authorization;
    }
}
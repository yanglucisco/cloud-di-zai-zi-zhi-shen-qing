package org.ziranziyuanting.authcenter.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredClientEntity extends CommonEntity {
    private String clientId;
    private String clientSecret;
    private String clientName;
    private String clientAuthenticationMethods;
    private String authorizationGrantTypes;
    private String redirectUri;
    private String postLogoutRedirectUri;
    private String scopes;
    private int refreshTokenTimeToLive;
    private int accessTokenTimeToLive;
    private int requireAuthorizationConsent;
    private int requireProofKey;
}

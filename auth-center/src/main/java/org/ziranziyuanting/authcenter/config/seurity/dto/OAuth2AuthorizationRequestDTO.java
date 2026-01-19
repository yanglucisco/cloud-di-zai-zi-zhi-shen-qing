package org.ziranziyuanting.authcenter.config.seurity.dto;

import java.util.Map;
import java.util.Set;

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2AuthorizationRequestDTO {
   private String authorizationUri;
   // private String authorizationGrantType;
   // private String responseType;
   private String clientId;
   private String redirectUri;
   private Set<String> scopes;
   private String state;
   private Map<String, Object> additionalParameters;
   private String authorizationRequestUri;
   private Map<String, Object> attributes;
   public static OAuth2AuthorizationRequestDTO from(OAuth2AuthorizationRequest r123){
      OAuth2AuthorizationRequestDTO dto2 = new OAuth2AuthorizationRequestDTO();
        dto2.additionalParameters=(r123.getAdditionalParameters());
        dto2.attributes=(r123.getAttributes());
      //   dto2.authorizationGrantType=(r123.getGrantType().getValue());
        dto2.authorizationRequestUri=(r123.getAuthorizationRequestUri());
        dto2.authorizationUri=(r123.getAuthorizationUri());
        dto2.clientId=(r123.getClientId());
        dto2.redirectUri=(r123.getRedirectUri());
      //   dto2.responseType=(r123.getResponseType().getValue());
        dto2.scopes=(r123.getScopes());
        dto2.state=(r123.getState());
      return dto2;
   }
   public OAuth2AuthorizationRequest to(){
      OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
               .additionalParameters(additionalParameters)
               .attributes(attributes)
               .authorizationRequestUri(authorizationRequestUri)
               .clientId(clientId)
               .redirectUri(redirectUri)
               .scopes(scopes)
               .state(state)
               .authorizationUri(authorizationUri)
                .build();
      return authorizationRequest;
   }
}

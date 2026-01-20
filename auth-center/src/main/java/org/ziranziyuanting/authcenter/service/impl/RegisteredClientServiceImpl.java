package org.ziranziyuanting.authcenter.service.impl;

import java.time.Duration;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.ziranziyuanting.authcenter.entity.RegisteredClientEntity;
import org.ziranziyuanting.authcenter.mapper.RegisteredClientMapper;
import org.ziranziyuanting.authcenter.service.RegisteredClientService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class RegisteredClientServiceImpl extends ServiceImpl<RegisteredClientMapper, RegisteredClientEntity> implements RegisteredClientService {

    @Override
    public org.springframework.security.oauth2.server.authorization.client.RegisteredClient findByClientId(String clientId) {
        RegisteredClientEntity entity = getOne(
            new LambdaQueryWrapper<>(
                RegisteredClientEntity.class).eq(RegisteredClientEntity::getClientId, clientId));
        return get(entity);
    }

    @Override
    public RegisteredClient findById(String id) {
        RegisteredClientEntity entity = getById(id);
        return get(entity);
    }
    private RegisteredClient get(RegisteredClientEntity entity)
    {
        var requireAuthorizationConsent = entity.getRequireAuthorizationConsent() == 1 ? true : false;
        var requireProofKey = entity.getRequireProofKey() == 1 ? true : false;
        var r = RegisteredClient.withId(entity.getId().toString())
                               .clientId(entity.getClientId())
                               .clientSecret(entity.getClientSecret())
                               .clientName(entity.getClientName())
                               .clientAuthenticationMethods(m -> {
                                        String[] clientAuthenticationMethods = entity.getClientAuthenticationMethods().split(",");
                                        for(String method : clientAuthenticationMethods){
                                            m.add(new ClientAuthenticationMethod(method));
                                        }
                                })
                                .authorizationGrantTypes(gts -> {
                                    String[] authorizationGrantTypes = entity.getAuthorizationGrantTypes().split(",");
                                        for(String type : authorizationGrantTypes){
                                            gts.add(new AuthorizationGrantType(type));
                                        }
                                })
                                .redirectUri(entity.getRedirectUri());
                                if(entity.getPostLogoutRedirectUri() != null){
                                    r.postLogoutRedirectUri(entity.getPostLogoutRedirectUri());
                                }
                                r
                                .scopes(s -> {
                                    var scopes = entity.getScopes().split(",");
                                    for(String scope : scopes){
                                        s.add(scope);
                                    }
                                })
                                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(requireAuthorizationConsent).build())
                                .clientSettings(ClientSettings.builder().requireProofKey(requireProofKey).build())
                                .tokenSettings(TokenSettings.builder().refreshTokenTimeToLive(Duration.ofMinutes(entity.getRefreshTokenTimeToLive())).build())
                                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(entity.getAccessTokenTimeToLive())).build())          
                                ;
        return r.build();
    }
    
}

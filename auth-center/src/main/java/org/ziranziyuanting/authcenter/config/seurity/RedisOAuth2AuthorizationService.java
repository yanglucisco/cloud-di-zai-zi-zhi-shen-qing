package org.ziranziyuanting.authcenter.config.seurity;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.ziranziyuanting.authcenter.config.seurity.dto.OAuth2AuthorizationDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

    // private static final Logger log =
    // LoggerFactory.getLogger(RedisOAuth2AuthorizationService.class);

    // Redis Key 前缀
    // private static final String AUTHORIZATION_KEY_PREFIX =
    // "oauth2:authorization:";
    private static final String AUTHORIZATION_CODE_KEY_PREFIX = "oauth2:authorization_code:";
    private static final String ACCESS_TOKEN_KEY_PREFIX = "oauth2:access_token:";
    private static final String REFRESH_TOKEN_KEY_PREFIX = "oauth2:refresh_token:";
    private static final String OIDC_TOKEN_KEY_PREFIX = "oauth2:oidc_token:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final RegisteredClientRepository registeredClientRepository;
    // private Map<String, OAuth2AuthorizationDTO> map = new HashMap<>();
    // private Map<String, OAuth2Authorization> map1 = new HashMap<>();
    // private OAuth2Authorization oAuth2Authorization = null;
    /**
     * 存放键值，键是3个（AUTHORIZATION_CODE、ACCESS_TOKEN_KEY、REFRESH_TOKEN_KEY）加token值，
     * 值是AUTHORIZATION_KEY_PREFIX + OAuth2Authorization.getId()
     */
    // private Map<String, String> indexMap = new HashMap<>();

    public RedisOAuth2AuthorizationService(
            RedisTemplate<String, Object> redisTemplate,
    RegisteredClientRepository registeredClientRepository
    ) {
        this.redisTemplate = redisTemplate;
        this.registeredClientRepository = registeredClientRepository;
    }

    /**
     * 保存授权信息
     */
    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        // oAuth2Authorization = authorization;
        // 主键
        String id = authorization.getId();
        OAuth2AuthorizationDTO dto = OAuth2AuthorizationDTO.from(authorization);
        // 存储主对象
        // // 建立索引
        indexAuthorization(authorization, id, dto);

        log.debug("Saved authorization with id: {}", id);
    }

    /**
     * 根据ID查找
     */
    @Override
    public OAuth2Authorization findById(String id) {
        Assert.hasText(id, "id cannot be empty");

        // String key = AUTHORIZATION_CODE_KEY_PREFIX + id;
        // OAuth2AuthorizationDTO dto = map.get(key);
        // (OAuth2AuthorizationDTO) redisTemplate.opsForValue().get(key);

        // if (dto == null) {
        // return null;
        // }

        // return dto.toOAuth2Authorization(registeredClientRepository);
        return null;
    }

    /**
     * 根据Token查找
     */
    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        String authorizationKey = findAuthorizationKeyByToken(token, tokenType);
        // String id = authorization.getId();
        // // map.put(id, authorization);
        // String key = AUTHORIZATION_CODE_KEY_PREFIX + id;
        // Assert.notNull(authorizationKey, "");
        // var r2 = redisTemplate.opsForValue().get(AUTHORIZATION_CODE_KEY_PREFIX +
        // "__1");
        Object obj = redisTemplate.opsForValue().get(authorizationKey);
        if(obj != null){
            
        }
        var r1 = (OAuth2AuthorizationDTO)redisTemplate.opsForValue().get(authorizationKey);
        var r2 = r1.toOAuth2Authorization(this.registeredClientRepository);
        // var r = r1.toOAuth2Authorization(r1);
        // var r =
        // (OAuth2Authorization)redisTemplate.opsForValue().get(authorizationKey);
        return r2;// this.oAuth2Authorization;
        // String id = OAuth2TokenType.
        // return map.get("tokenType");
        // if (tokenType == null) {
        // return findById(token);
        // }

        // // 根据Token类型查找Key
        // String authorizationKey = findAuthorizationKeyByToken(token, tokenType);
        // if (authorizationKey == null) {
        // return null;
        // }

        // // OAuth2AuthorizationDTO dto = (OAuth2AuthorizationDTO)
        // // redisTemplate.opsForValue().get(authorizationKey);

        // // OAuth2AuthorizationDTO dto = map.get(authorizationKey);

        // // if (dto == null) {
        // // return null;
        // // }

        // // var r = dto.toOAuth2Authorization(registeredClientRepository);
        // // var r = map1.get(authorizationKey);
        // // return r;
        // return oAuth2Authorization;
    }

    /**
     * 删除授权
     */
    @Override
    public void remove(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");

        String id = authorization.getId();
        // String key = AUTHORIZATION_CODE_KEY_PREFIX + id;

        try {
            // 删除主对象
            // redisTemplate.delete(key);
            // map1.remove(key);

            // 删除索引
            removeAuthorizationIndex(authorization);

            log.debug("Removed authorization with id: {}", id);

        } catch (Exception e) {
            log.error("Failed to remove authorization", e);
            throw new RuntimeException("Failed to remove authorization", e);
        }
    }
    private static Long redisTTL = 3000L;
    /**
     * 建立索引
     */
    private void indexAuthorization(OAuth2Authorization authorization, String id, OAuth2AuthorizationDTO dto
    // , String authorizationKey
    ) {
        // String key = "";// AUTHORIZATION_KEY_PREFIX + id;
        // 索引授权码
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
                .getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null) {
            String codeKey = AUTHORIZATION_CODE_KEY_PREFIX + authorizationCode.getToken().getTokenValue();
            String key = AUTHORIZATION_CODE_KEY_PREFIX + id;
            // indexMap.put(codeKey, key);
            // map1.put(key, authorization);
            redisTemplate.opsForValue().set(
                    codeKey,
                    key,
                    redisTTL,
                    TimeUnit.SECONDS);
                    redisTemplate.opsForValue().set(
                key,
                dto,
                redisTTL, // getExpireSeconds(authorization),
                TimeUnit.SECONDS);
        }
        //openid令牌
        OAuth2Authorization.Token<OidcIdToken> oidcIdToken = authorization
                .getToken(OidcIdToken.class);
        if(oidcIdToken != null){
            String codeKey = OIDC_TOKEN_KEY_PREFIX + oidcIdToken.getToken().getTokenValue();
            String key = OIDC_TOKEN_KEY_PREFIX + id;
            redisTemplate.opsForValue().set(
                    codeKey,
                    key,
                    redisTTL,
                    TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(
                key,
                dto,
                redisTTL, // getExpireSeconds(authorization),
                TimeUnit.SECONDS);
        }
        // 索引访问令牌
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
        if (accessToken != null) {
            String accessTokenKey = ACCESS_TOKEN_KEY_PREFIX + accessToken.getToken().getTokenValue();
            String key = ACCESS_TOKEN_KEY_PREFIX + id;
            // indexMap.put(accessTokenKey, key);
            // map1.put(key, authorization);
            // redisTemplate.opsForValue().set(
            // accessTokenKey,
            // authorizationKey,
            // accessToken.getToken().getExpiresAt()
            // .minusSeconds(Instant.now().getEpochSecond())
            // .getEpochSecond(),
            // TimeUnit.SECONDS
            // );
            redisTemplate.opsForValue().set(
                    accessTokenKey,
                    key,
                    redisTTL,
                    TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(
                key,
                dto,
                redisTTL, // getExpireSeconds(authorization),
                TimeUnit.SECONDS);
        }

        // 索引刷新令牌
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getToken(OAuth2RefreshToken.class);
        if (refreshToken != null) {
            String refreshTokenKey = REFRESH_TOKEN_KEY_PREFIX + refreshToken.getToken().getTokenValue();
            String key = REFRESH_TOKEN_KEY_PREFIX + id;
            // indexMap.put(refreshTokenKey, key);
            // map1.put(key, authorization);
            // redisTemplate.opsForValue().set(
            // refreshTokenKey,
            // authorizationKey,
            // refreshToken.getToken().getExpiresAt()
            // .minusSeconds(Instant.now().getEpochSecond())
            // .getEpochSecond(),
            // TimeUnit.SECONDS
            // );
            redisTemplate.opsForValue().set(
                    refreshTokenKey,
                    key,
                    redisTTL,
                    TimeUnit.SECONDS);
            redisTemplate.opsForValue().set(
                key,
                dto,
                redisTTL, // getExpireSeconds(authorization),
                TimeUnit.SECONDS);
        }
        
    }

    /**
     * 删除索引
     */
    private void removeAuthorizationIndex(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
                .getToken(OAuth2AuthorizationCode.class);

        if (authorizationCode != null) {
            String codeKey = AUTHORIZATION_CODE_KEY_PREFIX + authorizationCode.getToken().getTokenValue();
            // indexMap.remove(codeKey);
        }

        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);

        if (accessToken != null) {
            String accessTokenKey = ACCESS_TOKEN_KEY_PREFIX + accessToken.getToken().getTokenValue();
            // redisTemplate.delete(accessTokenKey);
            // indexMap.remove(accessTokenKey);
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getToken(OAuth2RefreshToken.class);

        if (refreshToken != null) {
            String refreshTokenKey = REFRESH_TOKEN_KEY_PREFIX + refreshToken.getToken().getTokenValue();
            // redisTemplate.delete(refreshTokenKey);
            // indexMap.remove(refreshTokenKey);
        }
    }

    /**
     * 根据Token查找Key
     */
    private String findAuthorizationKeyByToken(String token, OAuth2TokenType tokenType) {
        String indexKey = null;

        // if (OAuth2TokenType.AUTHORIZATION_CODE.equals(tokenType)) {
        // indexKey = AUTHORIZATION_CODE_KEY_PREFIX + token;
        // } else
        if (tokenType.getValue().equals("code")) {
            indexKey = AUTHORIZATION_CODE_KEY_PREFIX + token;
        }
        else if(tokenType.getValue().equals("id_token")){
            indexKey = OIDC_TOKEN_KEY_PREFIX + token;
        }
        else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            indexKey = ACCESS_TOKEN_KEY_PREFIX + token;
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            indexKey = REFRESH_TOKEN_KEY_PREFIX + token;
        }

        if (indexKey == null) {
            return null;
        }
        // return indexKey;
        return (String)redisTemplate.opsForValue().get(indexKey);
    }

    /**
     * 计算过期时间
     */
    private long getExpireSeconds(OAuth2Authorization authorization) {
        // 获取最晚过期时间
        Instant maxExpiry = Instant.MIN;

        // 授权码过期时间
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
                .getToken(OAuth2AuthorizationCode.class);
        if (authorizationCode != null && authorizationCode.getToken().getExpiresAt() != null) {
            maxExpiry = maxExpiry.isAfter(authorizationCode.getToken().getExpiresAt())
                    ? maxExpiry
                    : authorizationCode.getToken().getExpiresAt();
        }

        // 访问令牌过期时间
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
        if (accessToken != null && accessToken.getToken().getExpiresAt() != null) {
            maxExpiry = maxExpiry.isAfter(accessToken.getToken().getExpiresAt())
                    ? maxExpiry
                    : accessToken.getToken().getExpiresAt();
        }

        // 刷新令牌过期时间
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getToken(OAuth2RefreshToken.class);
        if (refreshToken != null && refreshToken.getToken().getExpiresAt() != null) {
            maxExpiry = maxExpiry.isAfter(refreshToken.getToken().getExpiresAt())
                    ? maxExpiry
                    : refreshToken.getToken().getExpiresAt();
        }

        if (maxExpiry.equals(Instant.MIN)) {
            // 默认5分钟
            return 300L;
        }

        long seconds = maxExpiry.getEpochSecond() - Instant.now().getEpochSecond();
        return seconds > 0 ? seconds : 300L; // 最小5分钟
    }
}
package org.ziranziyuanting.authcenter.config.seurity;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.*;

/**
 * 自定义Redis Session注册表
 * 支持Spring Authorization Server的OAuth2会话管理
 */
public class RedisSessionRegistry implements SessionRegistry {

    private static final String SESSION_KEY_PREFIX = "spring:session:sessions:";
    private static final String PRINCIPAL_SESSIONS_KEY_PREFIX = "spring:session:principals:";
    private static final String EXPIRED_SESSIONS_KEY = "spring:session:expired_sessions";

    private final RedisTemplate<String, Object> redisTemplate;
    private final long defaultMaxInactiveInterval; // 默认会话超时时间（秒）

    /**
     * 构造函数
     * @param redisTemplate Redis模板
     * @param defaultMaxInactiveInterval 默认会话超时时间（秒）
     */
    public RedisSessionRegistry(RedisTemplate<String, Object> redisTemplate, 
                                long defaultMaxInactiveInterval) {
        Assert.notNull(redisTemplate, "redisTemplate cannot be null");
        this.redisTemplate = redisTemplate;
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }

    @Override
    public List<Object> getAllPrincipals() {
        Set<String> keys = redisTemplate.keys(PRINCIPAL_SESSIONS_KEY_PREFIX + "*");
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        
        List<Object> principals = new ArrayList<>();
        for (String key : keys) {
            String principal = key.substring(PRINCIPAL_SESSIONS_KEY_PREFIX.length());
            principals.add(deserializePrincipal(principal));
        }
        return principals;
    }

    @Override
    public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
        Assert.notNull(principal, "principal cannot be null");
        
        String principalKey = getPrincipalSessionsKey(principal);
        Set<Object> sessionIds = redisTemplate.opsForSet().members(principalKey);
        
        if (CollectionUtils.isEmpty(sessionIds)) {
            return Collections.emptyList();
        }
        
        List<SessionInformation> sessions = new ArrayList<>();
        for (Object sessionIdObj : sessionIds) {
            String sessionId = (String) sessionIdObj;
            SessionInformation sessionInfo = getSessionInformation(sessionId);
            
            if (sessionInfo != null && (includeExpiredSessions || !sessionInfo.isExpired())) {
                sessions.add(sessionInfo);
            }
        }
        
        return sessions;
    }

    @SuppressWarnings({ "null" })
    @Override
    public SessionInformation getSessionInformation(String sessionId) {
        Assert.hasText(sessionId, "sessionId cannot be null or empty");
        
        String sessionKey = getSessionKey(sessionId);
        Object obj = redisTemplate.opsForValue().get(sessionKey);
        RedisSession session = (RedisSession)redisTemplate.opsForValue().get(sessionKey);
        // RedisSession session = RedisSession.builder()
        // .id(ops.get("id").toString())
        // .principal(ops.get(sessionKey))
        // .build();
        
        if (session == null) {
            return null;
        }
        
        // 检查会话是否已过期
        if (isSessionExpired(session)) {
            removeSessionInformation(sessionId);
            return null;
        }
        
        return new SessionInformation(
            session.getPrincipal(),
            sessionId,
            Date.from(session.getCreationTime())
        );
    }

    @Override
    public void refreshLastRequest(String sessionId) {
        Assert.hasText(sessionId, "sessionId cannot be null or empty");
        
        String sessionKey = getSessionKey(sessionId);
        RedisSession session = (RedisSession) redisTemplate.opsForValue().get(sessionKey);
        
        if (session != null) {
            session.setLastAccessTime(Instant.now());
            redisTemplate.opsForValue().set(sessionKey, session, defaultMaxInactiveInterval, 
                java.util.concurrent.TimeUnit.SECONDS);
        }
    }

    @SuppressWarnings("null")
    @Override
    public void registerNewSession(String sessionId, Object principal) {
        Assert.hasText(sessionId, "sessionId cannot be null or empty");
        Assert.notNull(principal, "principal cannot be null");
        
        Instant now = Instant.now();
        RedisSession redisSession = new RedisSession();
        redisSession.setId(sessionId);
        redisSession.setPrincipal(principal);
        redisSession.setCreationTime(now);
        redisSession.setLastAccessTime(now);
        redisSession.setMaxInactiveInterval(defaultMaxInactiveInterval);
        
        // 存储会话信息
        String sessionKey = getSessionKey(sessionId);
        redisTemplate.opsForValue().set(sessionKey, redisSession, 
            defaultMaxInactiveInterval, java.util.concurrent.TimeUnit.SECONDS);
        
        // 建立principal到session的映射
        String principalKey = getPrincipalSessionsKey(principal);
        redisTemplate.opsForSet().add(principalKey, sessionId);
        
        // 设置principal映射的过期时间
        redisTemplate.expire(principalKey, defaultMaxInactiveInterval, 
            java.util.concurrent.TimeUnit.SECONDS);
    }

    @Override
    public void removeSessionInformation(String sessionId) {
        Assert.hasText(sessionId, "sessionId cannot be null or empty");
        
        String sessionKey = getSessionKey(sessionId);
        RedisSession session = (RedisSession) redisTemplate.opsForValue().get(sessionKey);
        
        if (session != null) {
            // 从principal映射中移除
            String principalKey = getPrincipalSessionsKey(session.getPrincipal());
            redisTemplate.opsForSet().remove(principalKey, sessionId);
            
            // 如果该principal没有其他会话，删除principal键
            Long size = redisTemplate.opsForSet().size(principalKey);
            if (size != null && size == 0) {
                redisTemplate.delete(principalKey);
            }
            
            // 将会话标记为已过期
            session.setExpired(true);
            redisTemplate.opsForValue().set(sessionKey, session, 300, 
                java.util.concurrent.TimeUnit.SECONDS);
            
            // 记录到过期会话列表（用于清理）
            redisTemplate.opsForSet().add(EXPIRED_SESSIONS_KEY, sessionId);
        }
    }

    /**
     * 获取用户的所有有效会话数量
     */
    public int getActiveSessionCount(Object principal) {
        List<SessionInformation> sessions = getAllSessions(principal, false);
        return sessions.size();
    }

    /**
     * 强制使指定用户的所有会话过期
     */
    public void expireNowByPrincipal(Object principal) {
        List<SessionInformation> sessions = getAllSessions(principal, true);
        for (SessionInformation session : sessions) {
            session.expireNow();
            removeSessionInformation(session.getSessionId());
        }
    }

    /**
     * 清理过期会话
     */
    public void cleanExpiredSessions() {
        Set<Object> expiredSessionIds = redisTemplate.opsForSet().members(EXPIRED_SESSIONS_KEY);
        if (!CollectionUtils.isEmpty(expiredSessionIds)) {
            for (Object sessionIdObj : expiredSessionIds) {
                String sessionId = (String) sessionIdObj;
                redisTemplate.delete(getSessionKey(sessionId));
            }
            redisTemplate.delete(EXPIRED_SESSIONS_KEY);
        }
    }

    /**
     * 检查会话是否过期
     */
    private boolean isSessionExpired(RedisSession session) {
        if (session.isExpired()) {
            return true;
        }
        
        Instant now = Instant.now();
        Instant expireTime = session.getLastAccessTime()
            .plusSeconds(session.getMaxInactiveInterval());
        
        return now.isAfter(expireTime);
    }

    /**
     * 获取会话在Redis中的key
     */
    private String getSessionKey(String sessionId) {
        return SESSION_KEY_PREFIX + sessionId;
    }

    /**
     * 获取principal在Redis中的key
     */
    private String getPrincipalSessionsKey(Object principal) {
        String principalName = serializePrincipal(principal);
        return PRINCIPAL_SESSIONS_KEY_PREFIX + principalName;
    }

    /**
     * 序列化principal对象
     */
    private String serializePrincipal(Object principal) {
        if (principal instanceof String) {
            return (String) principal;
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    /**
     * 反序列化principal对象
     */
    private Object deserializePrincipal(String principalString) {
        // 这里可以根据需要实现更复杂的反序列化逻辑
        return principalString;
    }

    /**
     * Redis存储的会话对象
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RedisSession implements java.io.Serializable {
        private String id;
        private Object principal;
        private Instant creationTime;
        private Instant lastAccessTime;
        private long maxInactiveInterval;
        private boolean expired = false;
        // private Map<String, Object> attributes = new ConcurrentHashMap<>();
    }
}

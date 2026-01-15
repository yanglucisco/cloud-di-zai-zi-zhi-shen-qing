package org.ziranziyuanting.authcenter.config.seurity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于内存 Map 的 SessionRegistry 实现
 * 适用于单节点部署的 OAuth2 授权服务器
 */
// @Component
@Slf4j
public class InMemorySessionRegistry implements SessionRegistry {
    
    // private static final Logger log = LoggerFactory.getLogger(InMemorySessionRegistry.class);
    
    // 核心存储结构
    private final Map<String, SessionInformation> sessionInfoMap = new ConcurrentHashMap<>();
    private final Map<String, List<String>> principalToSessionIdsMap = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> sessionIdToPrincipalsMap = new ConcurrentHashMap<>();
    
    // 锁，用于并发控制
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    
    // 会话过期时间（秒），默认30分钟
    private int sessionTimeout = 30 * 60;
    
    // 清理线程
    private ScheduledExecutorService cleanupScheduler;
    
    public InMemorySessionRegistry() {
        this(30 * 60);
    }
    
    public InMemorySessionRegistry(int sessionTimeoutSeconds) {
        this.sessionTimeout = sessionTimeoutSeconds;
        startCleanupTask();
    }
    
    /**
     * 注册新会话
     */
    @Override
    public void registerNewSession(String sessionId, Object principal) {
        Assert.hasText(sessionId, "SessionId must not be null or empty");
        Assert.notNull(principal, "Principal must not be null");
        
        String principalName = getPrincipalName(principal);
        
        writeLock.lock();
        try {
            // 1. 创建会话信息
            SessionInformation sessionInfo = new SessionInformation(
                principal, 
                sessionId, 
                Date.from(Instant.now())
            );
            
            // 2. 存储到 Map
            sessionInfoMap.put(sessionId, sessionInfo);
            
            // 3. 建立 Principal 到 Session 的映射
            principalToSessionIdsMap
                .computeIfAbsent(principalName, k -> new CopyOnWriteArrayList<>())
                .add(sessionId);
            
            // 4. 建立 Session 到 Principal 的映射
            sessionIdToPrincipalsMap
                .computeIfAbsent(sessionId, k -> ConcurrentHashMap.newKeySet())
                .add(principalName);
            
            log.debug("Registered new session [{}] for principal [{}]", sessionId, principalName);
            log.debug("Total sessions: {}, Total principals: {}", 
                sessionInfoMap.size(), principalToSessionIdsMap.size());
            
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * 移除会话信息
     */
    @Override
    public void removeSessionInformation(String sessionId) {
        if (sessionId == null) {
            return;
        }
        
        writeLock.lock();
        try {
            // 1. 获取会话信息
            SessionInformation sessionInfo = sessionInfoMap.remove(sessionId);
            if (sessionInfo == null) {
                return;
            }
            
            String principalName = getPrincipalName(sessionInfo.getPrincipal());
            
            // 2. 从 Principal 映射中移除
            List<String> sessionIds = principalToSessionIdsMap.get(principalName);
            if (sessionIds != null) {
                sessionIds.remove(sessionId);
                if (sessionIds.isEmpty()) {
                    principalToSessionIdsMap.remove(principalName);
                }
            }
            
            // 3. 移除 Session 到 Principal 的映射
            sessionIdToPrincipalsMap.remove(sessionId);
            
            log.debug("Removed session [{}] for principal [{}]", sessionId, principalName);
            
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * 获取会话信息
     */
    @Override
    public SessionInformation getSessionInformation(String sessionId) {
        if (sessionId == null) {
            return null;
        }
        
        readLock.lock();
        try {
            SessionInformation info = sessionInfoMap.get(sessionId);
            
            // 检查会话是否过期
            if (info != null && info.isExpired()) {
                // 如果已过期，异步清理
                Executors.newSingleThreadExecutor().submit(() -> 
                    removeSessionInformation(sessionId)
                );
                return null;
            }
            
            return info;
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * 获取所有 Principal
     */
    @Override
    public List<Object> getAllPrincipals() {
        readLock.lock();
        try {
            List<Object> principals = new ArrayList<>();
            for (String principalName : principalToSessionIdsMap.keySet()) {
                // 创建一个简单的 Principal 对象
                principals.add(new SimplePrincipal(principalName));
            }
            return Collections.unmodifiableList(principals);
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * 获取指定 Principal 的所有会话
     */
    @Override
    public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
        if (principal == null) {
            return Collections.emptyList();
        }
        
        String principalName = getPrincipalName(principal);
        
        readLock.lock();
        try {
            List<String> sessionIds = principalToSessionIdsMap.get(principalName);
            if (sessionIds == null || sessionIds.isEmpty()) {
                return Collections.emptyList();
            }
            
            List<SessionInformation> sessions = new ArrayList<>();
            for (String sessionId : sessionIds) {
                SessionInformation sessionInfo = sessionInfoMap.get(sessionId);
                if (sessionInfo != null) {
                    if (includeExpiredSessions || !sessionInfo.isExpired()) {
                        sessions.add(sessionInfo);
                    }
                }
            }
            
            return Collections.unmodifiableList(sessions);
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * 刷新会话的最后请求时间
     */
    public void refreshLastRequest(String sessionId) {
        if (sessionId == null) {
            return;
        }
        
        writeLock.lock();
        try {
            SessionInformation sessionInfo = sessionInfoMap.get(sessionId);
            if (sessionInfo != null) {
                sessionInfo.refreshLastRequest();
                log.debug("Refreshed last request for session [{}]", sessionId);
            }
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * 使指定会话过期
     */
    public void expireNow(String sessionId) {
        if (sessionId == null) {
            return;
        }
        
        writeLock.lock();
        try {
            SessionInformation sessionInfo = sessionInfoMap.get(sessionId);
            if (sessionInfo != null && !sessionInfo.isExpired()) {
                sessionInfo.expireNow();
                log.info("Session [{}] has been expired", sessionId);
                
                // 触发会话销毁监听
                onSessionExpired(sessionId, sessionInfo.getPrincipal());
            }
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * 使指定 Principal 的所有会话过期
     */
    public void expireSessionsForPrincipal(String principalName) {
        if (principalName == null) {
            return;
        }
        
        writeLock.lock();
        try {
            List<String> sessionIds = principalToSessionIdsMap.get(principalName);
            if (sessionIds != null) {
                for (String sessionId : sessionIds) {
                    expireNow(sessionId);
                }
                log.info("All sessions for principal [{}] have been expired", principalName);
            }
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * 获取当前认证用户的会话
     */
    public Optional<SessionInformation> getCurrentUserSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String principalName = getPrincipalName(principal);
            
            List<SessionInformation> sessions = getAllSessions(principal, false);
            if (!sessions.isEmpty()) {
                // 返回最近创建的会话
                return sessions.stream()
                    .max(Comparator.comparing(SessionInformation::getLastRequest));
            }
        }
        return Optional.empty();
    }
    
    /**
     * 获取会话统计信息
     */
    public SessionStats getSessionStats() {
        readLock.lock();
        try {
            int totalSessions = sessionInfoMap.size();
            int activeSessions = 0;
            int expiredSessions = 0;
            
            for (SessionInformation session : sessionInfoMap.values()) {
                if (session.isExpired()) {
                    expiredSessions++;
                } else {
                    activeSessions++;
                }
            }
            
            int totalPrincipals = principalToSessionIdsMap.size();
            
            return new SessionStats(totalPrincipals, totalSessions, activeSessions, expiredSessions);
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * 清理过期会话
     */
    public void cleanupExpiredSessions() {
        writeLock.lock();
        try {
            int count = 0;
            Iterator<Map.Entry<String, SessionInformation>> iterator = 
                sessionInfoMap.entrySet().iterator();
            
            while (iterator.hasNext()) {
                Map.Entry<String, SessionInformation> entry = iterator.next();
                String sessionId = entry.getKey();
                SessionInformation sessionInfo = entry.getValue();
                
                if (sessionInfo.isExpired()) {
                    iterator.remove();
                    
                    // 清理映射关系
                    String principalName = getPrincipalName(sessionInfo.getPrincipal());
                    List<String> sessionIds = principalToSessionIdsMap.get(principalName);
                    if (sessionIds != null) {
                        sessionIds.remove(sessionId);
                        if (sessionIds.isEmpty()) {
                            principalToSessionIdsMap.remove(principalName);
                        }
                    }
                    
                    sessionIdToPrincipalsMap.remove(sessionId);
                    count++;
                    
                    onSessionExpired(sessionId, sessionInfo.getPrincipal());
                }
            }
            
            if (count > 0) {
                log.info("Cleaned up {} expired sessions", count);
            }
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * 启动定期清理任务
     */
    private void startCleanupTask() {
        cleanupScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "session-cleanup-thread");
            thread.setDaemon(true);
            return thread;
        });
        
        // 每5分钟清理一次过期会话
        cleanupScheduler.scheduleAtFixedRate(() -> {
            try {
                cleanupExpiredSessions();
            } catch (Exception e) {
                log.error("Error during session cleanup", e);
            }
        }, 5, 5, TimeUnit.MINUTES);
        
        // 关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (cleanupScheduler != null && !cleanupScheduler.isShutdown()) {
                cleanupScheduler.shutdown();
                try {
                    if (!cleanupScheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                        cleanupScheduler.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    cleanupScheduler.shutdownNow();
                }
            }
        }));
    }
    
    /**
     * 会话过期时的回调
     */
    protected void onSessionExpired(String sessionId, Object principal) {
        // 子类可以重写此方法
        log.debug("Session expired callback: sessionId={}, principal={}", 
            sessionId, getPrincipalName(principal));
    }
    
    /**
     * 获取 Principal 名称
     */
    private String getPrincipalName(Object principal) {
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof Authentication) {
            Object authPrincipal = ((Authentication) principal).getPrincipal();
            if (authPrincipal instanceof UserDetails) {
                return ((UserDetails) authPrincipal).getUsername();
            }
            return authPrincipal.toString();
        } else if (principal instanceof String) {
            return (String) principal;
        } else {
            return principal.toString();
        }
    }
    
    /**
     * 简单 Principal 对象
     */
    public static class SimplePrincipal {
        private final String name;
        
        public SimplePrincipal(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        @Override
        public String toString() {
            return name;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SimplePrincipal that = (SimplePrincipal) o;
            return Objects.equals(name, that.name);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
    
    /**
     * 会话统计信息
     */
    public static class SessionStats {
        private final int totalPrincipals;
        private final int totalSessions;
        private final int activeSessions;
        private final int expiredSessions;
        
        public SessionStats(int totalPrincipals, int totalSessions, 
                           int activeSessions, int expiredSessions) {
            this.totalPrincipals = totalPrincipals;
            this.totalSessions = totalSessions;
            this.activeSessions = activeSessions;
            this.expiredSessions = expiredSessions;
        }
        
        public int getTotalPrincipals() { return totalPrincipals; }
        public int getTotalSessions() { return totalSessions; }
        public int getActiveSessions() { return activeSessions; }
        public int getExpiredSessions() { return expiredSessions; }
        
        @Override
        public String toString() {
            return String.format(
                "SessionStats{principals=%d, total=%d, active=%d, expired=%d}",
                totalPrincipals, totalSessions, activeSessions, expiredSessions
            );
        }
    }
    
    /**
     * 销毁方法
     */
    @PreDestroy
    public void destroy() {
        if (cleanupScheduler != null && !cleanupScheduler.isShutdown()) {
            cleanupScheduler.shutdown();
            try {
                if (!cleanupScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    cleanupScheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                cleanupScheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        // 清理所有数据
        clearAll();
        log.info("Session registry destroyed");
    }
    
    /**
     * 清理所有会话数据
     */
    public void clearAll() {
        writeLock.lock();
        try {
            sessionInfoMap.clear();
            principalToSessionIdsMap.clear();
            sessionIdToPrincipalsMap.clear();
            log.info("All session data cleared");
        } finally {
            writeLock.unlock();
        }
    }
}
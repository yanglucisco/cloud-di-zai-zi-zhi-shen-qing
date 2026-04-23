package org.ziranziyuanting.account.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactiveRedisService {
    
    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;
    private final ObjectMapper objectMapper;
    
    
    /**
     * 读取列表
     */
    public Flux<String> getFluxFromList(String key) {
        return reactiveRedisTemplate.opsForList()
            .range(key, 0, -1)  // 获取所有元素
            .map(String::valueOf)  // 转换为String
            .doOnSubscribe(sub -> log.debug("开始读取列表: key={}", key))
            .doOnComplete(() -> log.debug("列表读取完成: key={}", key));
    }
    /**
     * 设置缓存（响应式）
     */
    public Mono<Boolean> set(String key, Object value, Duration timeout) {
        return reactiveRedisTemplate.opsForValue()
                .set(key, value, timeout)
                .doOnSuccess(success -> {
                    if (success) {
                        log.debug("Redis 设置成功: key={}", key);
                    } else {
                        log.warn("Redis 设置失败: key={}", key);
                    }
                })
                .doOnError(error -> 
                    log.error("Redis 设置异常: key={}, error={}", key, error.getMessage()));
    }
    
    /**
     * 获取缓存（响应式）
     */
    public Mono<Object> get(String key) {
        return reactiveRedisTemplate.opsForValue()
                .get(key)
                .doOnNext(value -> 
                    log.debug("Redis 获取: key={}, value={}", key, value))
                .doOnError(error -> 
                    log.error("Redis 获取异常: key={}, error={}", key, error.getMessage()));
    }
    
    /**
     * 获取并转换为指定类型
     */
    public <T> Mono<T> getAs(String key, Class<T> clazz) {
        return get(key)
                .flatMap(value -> {
                    try {
                        if (clazz.isInstance(value)) {
                            return Mono.just(clazz.cast(value));
                        } else {
                            // 尝试通过 ObjectMapper 转换
                            String json = objectMapper.writeValueAsString(value);
                            T result = objectMapper.readValue(json, clazz);
                            return Mono.just(result);
                        }
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("转换失败: " + e.getMessage()));
                    }
                })
                .doOnNext(result -> 
                    log.debug("Redis 获取并转换: key={}, class={}", key, clazz.getSimpleName()));
    }

    /**
     * 方法1：将 Flux<String> 转换为 List 后存储
     */
    public <T> Mono<Boolean> saveFluxAsList(String key, Flux<T> dataFlux, Class<T> clazz) {
        return dataFlux
            .collectList()  // 将 Flux 转换为 Mono<List<String>>
            .flatMap(list -> 
                reactiveRedisTemplate.opsForList()
                    .leftPushAll(key, list.toArray())  // 存储为列表
                    .map(count -> {
                        log.info("存储列表成功: key={}, 元素数量={}", key, count);
                        return count > 0;
                    })
            )
            .flatMap(success -> 
                reactiveRedisTemplate.expire(key, Duration.ofHours(1))  // 设置过期时间
                    .thenReturn(success)
            )
            .doOnSuccess(success -> 
                log.info("Flux存储为列表完成: key={}, success={}", key, success))
            .doOnError(error -> 
                log.error("存储Flux失败: key={}, error={}", key, error.getMessage()));
    }
    
    /**
     * 删除缓存
     */
    public Mono<Long> delete(String key) {
        return reactiveRedisTemplate.delete(key)
                .doOnSuccess(count -> 
                    log.debug("Redis 删除: key={}, count={}", key, count))
                .doOnError(error -> 
                    log.error("Redis 删除异常: key={}, error={}", key, error.getMessage()));
    }
    
    /**
     * 检查键是否存在
     */
    public Mono<Boolean> hasKey(String key) {
        return reactiveRedisTemplate.hasKey(key)
                .doOnSuccess(exists -> 
                    log.debug("Redis 键是否存在: key={}, exists={}", key, exists))
                .doOnError(error -> 
                    log.error("Redis 检查键异常: key={}, error={}", key, error.getMessage()));
    }
    
    /**
     * 设置过期时间
     */
    public Mono<Boolean> expire(String key, Duration timeout) {
        return reactiveRedisTemplate.expire(key, timeout)
                .doOnSuccess(success -> 
                    log.debug("Redis 设置过期时间: key={}, success={}", key, success))
                .doOnError(error -> 
                    log.error("Redis 设置过期时间异常: key={}, error={}", key, error.getMessage()));
    }
    
    /**
     * 发布消息
     */
    public Mono<Long> publish(String channel, Object message) {
        return reactiveRedisTemplate.convertAndSend(channel, message)
                .doOnSuccess(count -> 
                    log.debug("Redis 发布消息: channel={}, count={}", channel, count))
                .doOnError(error -> 
                    log.error("Redis 发布消息异常: channel={}, error={}", channel, error.getMessage()));
    }
    
    /**
     * 哈希操作 - 设置字段
     */
    public Mono<Boolean> hSet(String key, String field, Object value) {
        return reactiveRedisTemplate.opsForHash()
                .put(key, field, value)
                .doOnSuccess(success -> 
                    log.debug("Redis Hash 设置: key={}, field={}, success={}", key, field, success))
                .doOnError(error -> 
                    log.error("Redis Hash 设置异常: key={}, field={}, error={}", 
                        key, field, error.getMessage()));
    }
    
    /**
     * 哈希操作 - 获取字段
     */
    public Mono<Object> hGet(String key, String field) {
        return reactiveRedisTemplate.opsForHash()
                .get(key, field)
                .doOnNext(value -> 
                    log.debug("Redis Hash 获取: key={}, field={}, value={}", key, field, value))
                .doOnError(error -> 
                    log.error("Redis Hash 获取异常: key={}, field={}, error={}", 
                        key, field, error.getMessage()));
    }
    
    /**
     * 列表操作 - 左侧添加
     */
    public Mono<Long> lPush(String key, Object value) {
        return reactiveRedisTemplate.opsForList()
                .leftPush(key, value)
                .doOnNext(size -> 
                    log.debug("Redis List 左侧添加: key={}, size={}", key, size))
                .doOnError(error -> 
                    log.error("Redis List 添加异常: key={}, error={}", key, error.getMessage()));
    }
    
    /**
     * 批量删除匹配的键
     */
    public Mono<Long> deleteByPattern(String pattern) {
        return reactiveRedisTemplate.keys(pattern)
                .collectList()
                .flatMap(keys -> {
                    if (keys.isEmpty()) {
                        return Mono.just(0L);
                    }
                    return reactiveRedisTemplate.delete(keys.toArray(new String[0]))
                            .map(Long::valueOf);
                })
                .doOnSuccess(count -> 
                    log.debug("Redis 批量删除: pattern={}, count={}", pattern, count))
                .doOnError(error -> 
                    log.error("Redis 批量删除异常: pattern={}, error={}", pattern, error.getMessage()));
    }
}

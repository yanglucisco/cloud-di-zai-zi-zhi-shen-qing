package org.ziranziyuanting.account.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.ziranziyuanting.account.config.ReactiveRedisService;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;
import org.ziranziyuanting.account.repository.SysOrgRepository;
import org.ziranziyuanting.account.service.SysOrgService;
import org.ziranziyuanting.common.service.impl.CommonServiceImpl;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@CacheConfig(cacheNames = "userCache") 
public class SysOrgServiceImpl extends CommonServiceImpl<SysOrg> implements SysOrgService {
    private final ReactiveRedisService reactiveRedisService;
    public SysOrgServiceImpl(SysOrgRepository repository, ReactiveRedisService reactiveRedisService){
        super(repository);
        this.reactiveRedisService = reactiveRedisService;
    }
    @Override
    public Mono<SysOrg> save(AddOrgParam parm) {
        SysOrg org = SysOrg.of(parm.getSortCode(), parm.getParentId(), parm.getName(), parm.getCategory());
        return saveOrUpdate(org);
    }
    @Override
    public Mono<SysOrg> update(AddOrgParam parm) {
        return findById(parm.getId()).flatMap(item -> {
            item.setSortCode(parm.getSortCode());
            item.setParentId(parm.getParentId());
            item.setName(parm.getName());
            item.setCategory(parm.getCategory());
            return saveOrUpdate(item);
        });
    }
    @Override
    public void test() {
        SysOrg org = SysOrg.of(1, 0L, "测试组织" + LocalDateTime.now(), "测试类别");
        saveOrUpdate(org).flatMap(item -> {
            Mono<SysOrg> r = findById(item.getId());
            return r;
        }).flatMap(item -> {
            item.setUpdateTime(LocalDateTime.now());
            return saveOrUpdate(item);
        }).subscribe(item -> {
            log.info("测试完成: {}", item);
        });
    }
    @Override
    public Mono<SysOrg> findAllOrgs(int id, int size) {
        String cacheKey = "reactive:user:" + id;
        
        return reactiveRedisService.getAs(cacheKey, SysOrg.class)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("缓存未命中，从数据库查询用户: {}", id);
                    return repository.findById(662517722747375616L)
                            .flatMap(user -> 
                                reactiveRedisService.set(cacheKey, user, Duration.ofMinutes(5))
                                    .thenReturn(user)
                            );
                }))
                .doOnNext(user -> 
                    log.info("获取到用户: {}", user.getName()));
    }
}
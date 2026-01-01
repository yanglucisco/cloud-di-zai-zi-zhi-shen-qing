package org.ziranziyuanting.account.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;
import org.ziranziyuanting.account.repository.SysOrgRepository;
import org.ziranziyuanting.account.service.SysOrgService;
import org.ziranziyuanting.common.CommonSnowflake;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SysOrgServiceImpl
 extends CommonServiceImpl<SysOrg> 
 implements SysOrgService {
    private final SysOrgRepository repository;
    @Autowired
    private CommonSnowflake snowflake;
    public SysOrgServiceImpl(SysOrgRepository repository){
        super(repository);
        this.repository = repository;
    }
    @Override
    public Mono<SysOrg> saveOrUpdate(SysOrg t) {
        if(t.getId() == null) {
            t.setId(snowflake.nextId());
            t.setNew(true);
            return repository.save(t);
        }
        t.setNew(false);
        return repository.save(t);
    }
    @Override
    public Mono<SysOrg> save(AddOrgParam parm) {
        SysOrg org = SysOrg.of(parm.getSortCode(), parm.getParentId(), parm.getName(), parm.getCategory());
        return saveOrUpdate(org);
    }
    @SuppressWarnings("null")
    @Override
    public Mono<SysOrg> update(AddOrgParam parm) {
        return repository.findById(parm.getId()).flatMap(item -> {
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
            @SuppressWarnings("null")
            Mono<SysOrg> r = repository.findById(item.getId());
            return r;
        }).flatMap(item -> {
            item.setUpdateTime(LocalDateTime.now());
            return saveOrUpdate(item);
        }).subscribe(item -> {
            log.info("测试完成: {}", item);
        });
    }
    @Override
    public Flux<SysOrg> findAll() {
        return repository.findAll();
    }
}

package org.ziranziyuanting.account.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
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
public class SysOrgServiceImpl extends CommonServiceImpl<SysOrg> implements SysOrgService {
    private final SysOrgRepository repository;
    // private final CommonTest commonTest;
    // private final CommonSnowflake snowflake;

    public SysOrgServiceImpl(SysOrgRepository repository){
        super(repository);
        this.repository = repository;
        // this.commonTest = commonTest;
        // this.snowflake = snowflake;
    }

    @Override
    public Flux<SysOrg> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<SysOrg> save(AddOrgParam parm) {
        Long id = 1l;//snowflake.nextId();
        SysOrg org = SysOrg.of(parm.getSortCode(), parm.getParentId(), parm.getName(), parm.getCategory());
        org.setId(id);
        return repository.save(org);
    }

    @SuppressWarnings("null")
    @Override
    public void test() {
        SysOrg org = SysOrg.of(1, 0, "测试组织" + LocalDateTime.now(), "测试类别");
        saveWithCheck(org).flatMap(item -> {
            Mono<SysOrg> r = repository.findById(item.getId());
            return r;
        }).flatMap(item -> {
            item.setUpdateTime(LocalDateTime.now());
            return saveWithCheck(item);
        }).subscribe(item -> {
            log.info("测试完成: {}", item);
        });
    }
}

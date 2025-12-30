package org.ziranziyuanting.account.service.impl;

import org.springframework.stereotype.Service;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;
import org.ziranziyuanting.account.repository.SysOrgRepository;
import org.ziranziyuanting.account.service.SysOrgService;
import org.ziranziyuanting.common.config.CommonTest;

import cn.hutool.core.lang.Snowflake;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SysOrgServiceImpl implements SysOrgService {
    private final SysOrgRepository repository;
    private final CommonTest commonTest;
    private final Snowflake snowflake;
    public SysOrgServiceImpl(SysOrgRepository repository, CommonTest commonTest, Snowflake snowflake){
        this.repository = repository; 
        this.commonTest = commonTest;
        this.snowflake = snowflake;
    }
    @Override
    public Flux<SysOrg> findAll(){
        return repository.findAll();
    }
    @Override
    public Mono<SysOrg> save(AddOrgParam parm){
        commonTest.print();
        Long id = snowflake.nextId();
        SysOrg org = SysOrg.of(parm.getSortCode(), parm.getParentId(), parm.getName(), parm.getCategory());
        org.setId(id.toString());
        return repository.save(org);
    }
}

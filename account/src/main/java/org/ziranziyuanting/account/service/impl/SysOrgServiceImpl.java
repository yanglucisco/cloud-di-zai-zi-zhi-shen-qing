package org.ziranziyuanting.account.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.entity.YlTest;
import org.ziranziyuanting.account.param.AddOrgParam;
import org.ziranziyuanting.account.repository.SysOrgRepository;
import org.ziranziyuanting.account.repository.YlTestRepository;
import org.ziranziyuanting.account.service.SysOrgService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SysOrgServiceImpl implements SysOrgService {
    private final SysOrgRepository repository;
    private final YlTestRepository ylTestRepository;
    public SysOrgServiceImpl(SysOrgRepository repository, YlTestRepository ylTestRepository){
        this.repository = repository; 
        this.ylTestRepository = ylTestRepository;
    }
    @Override
    public Flux<SysOrg> findAll(){
        return repository.findAll();
    }
    @Override
    public void save(AddOrgParam parm){
        SysOrg org = SysOrg.of(parm.getSortCode(), parm.getParentId(), parm.getName(), parm.getCategory());
        org.setId("1");
        repository.save(org);
    }
}

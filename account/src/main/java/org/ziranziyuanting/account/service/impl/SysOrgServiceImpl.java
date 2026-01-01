package org.ziranziyuanting.account.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;
import org.ziranziyuanting.account.repository.SysOrgRepository;
import org.ziranziyuanting.account.service.SysOrgService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SysOrgServiceImpl extends CommonServiceImpl<SysOrg> implements SysOrgService {
    public SysOrgServiceImpl(SysOrgRepository repository){
        super(repository);
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
    public Flux<SysOrg> findAll() {
        return findAll();
    }
}

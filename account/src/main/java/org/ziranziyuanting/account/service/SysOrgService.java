package org.ziranziyuanting.account.service;

import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysOrgService {

    Flux<SysOrg> findAll();

    Mono<SysOrg> save(AddOrgParam parm);
    
}

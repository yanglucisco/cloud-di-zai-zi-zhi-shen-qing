package org.ziranziyuanting.account.service;

import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;

import reactor.core.publisher.Flux;

public interface SysOrgService {

    Flux<SysOrg> findAll();

    void save(AddOrgParam parm);
    
}

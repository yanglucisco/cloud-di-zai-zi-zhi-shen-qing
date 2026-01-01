package org.ziranziyuanting.account.service;

import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;

import reactor.core.publisher.Mono;

public interface SysOrgService extends CommonService<SysOrg>
{
    Mono<SysOrg> save(AddOrgParam parm);
    void test();
    Mono<SysOrg> update(AddOrgParam parm);
}

package org.ziranziyuanting.account.service;

import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;
import org.ziranziyuanting.account.param.PageParam;
import org.ziranziyuanting.account.vo.SysOrgVO;
import org.ziranziyuanting.common.service.CommonService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysOrgService extends CommonService<SysOrg>
{
    Mono<SysOrg> save(AddOrgParam parm);
    void test();
    Mono<SysOrg> update(AddOrgParam parm);
    Flux<SysOrgVO> orgTree();
    /**
     * Find organizations with pagination
     */
    Flux<SysOrg> findOrgsByPage(PageParam pageParam);
    
    /**
     * Get total count of organizations
     */
    Mono<Long> countOrgs();
    Mono<Long> countOrgsByName(String name);
}

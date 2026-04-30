package org.ziranziyuanting.account.service;

import java.util.List;

import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;
import org.ziranziyuanting.account.param.PageParam;
import org.ziranziyuanting.account.vo.SysOrgTreeNodeVO;
import org.ziranziyuanting.account.vo.SysOrgVO;
import org.ziranziyuanting.common.service.CommonService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysOrgService extends CommonService<SysOrg>
{
    Mono<SysOrg> save(AddOrgParam parm);
    void test();
    Mono<SysOrg> update(AddOrgParam parm);
    Flux<SysOrgTreeNodeVO> orgTree();
    /**
     * Find organizations with pagination
     */
    Flux<SysOrgVO> findOrgsByPage(PageParam pageParam);
    
    /**
     * Get total count of organizations
     */
    Mono<Long> countOrgs();
    Mono<Long> countOrgsByName(String name, Long parentId);

    /**
     * Logically delete an organization.
     * @param id The ID of the organization to delete.
     * @return A Mono indicating success or failure.
     */
    public Mono<String> logicalDelete(List<Long> ids);
}

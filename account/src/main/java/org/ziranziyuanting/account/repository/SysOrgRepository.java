package org.ziranziyuanting.account.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.r2dbc.repository.Query;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.common.repository.CommonReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface SysOrgRepository extends CommonReactiveCrudRepository<SysOrg>
//  extends ReactiveCrudRepository<SysOrg, Long>
 {
    @Query("SELECT * FROM sys_org ORDER BY id LIMIT 10 OFFSET 0")
    Flux<SysOrg> findAllBy(PageRequest pageable);
}

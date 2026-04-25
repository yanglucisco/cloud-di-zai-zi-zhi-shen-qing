package org.ziranziyuanting.account.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.common.repository.CommonReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface SysOrgRepository extends CommonReactiveCrudRepository<SysOrg>
//  extends ReactiveCrudRepository<SysOrg, Long>
 {
    /**
     * Find all organizations with pagination.
     * @param pageable Contains page number and size information.
     * @return Flux of SysOrg entities.
     */
    @Query("SELECT * FROM sys_org ORDER BY id LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}")
    Flux<SysOrg> findAllBy(Pageable pageable);
}

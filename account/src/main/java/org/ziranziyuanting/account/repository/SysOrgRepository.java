package org.ziranziyuanting.account.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.ziranziyuanting.account.entity.SysOrg;

public interface SysOrgRepository extends ReactiveCrudRepository<SysOrg, Long>{
    
}

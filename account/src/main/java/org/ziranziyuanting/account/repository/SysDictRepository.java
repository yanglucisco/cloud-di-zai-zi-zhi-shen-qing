package org.ziranziyuanting.account.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.ziranziyuanting.account.entity.SysDict;
import org.ziranziyuanting.common.repository.CommonReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface SysDictRepository extends CommonReactiveCrudRepository<SysDict>
{
    @Query("select d1.* from sys_dict d1 inner join sys_dict d2 on d1.PARENT_ID = d2.id and d2.DICT_VALUE = :dictValue")
    Flux<SysDict> findByType(String dictValue);
}

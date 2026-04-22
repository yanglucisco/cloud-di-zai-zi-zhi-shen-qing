package org.ziranziyuanting.account.service;

import org.ziranziyuanting.account.entity.SysDict;
import org.ziranziyuanting.account.vo.SysDictVO;
import org.ziranziyuanting.common.service.CommonService;

import reactor.core.publisher.Mono;

public interface SysDictService extends CommonService<SysDict>
{

    Mono<SysDictVO> findAllVO();
    // Mono<SysDict> save(AddOrgParam parm);
    // void test();
    // Mono<SysDict> update(AddOrgParam parm);
}

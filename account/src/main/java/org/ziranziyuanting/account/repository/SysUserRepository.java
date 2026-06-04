package org.ziranziyuanting.account.repository;

import org.ziranziyuanting.account.entity.SysUser;
import org.ziranziyuanting.common.repository.CommonReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface SysUserRepository extends CommonReactiveCrudRepository<SysUser>
{
    Mono<SysUser> findByAccount(String account);
}

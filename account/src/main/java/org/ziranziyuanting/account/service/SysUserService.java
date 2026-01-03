package org.ziranziyuanting.account.service;

import org.ziranziyuanting.account.entity.SysUser;
import org.ziranziyuanting.account.param.SysUserParam;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

public interface SysUserService extends CommonService<SysUser>
{

    Mono<String> add(SysUserParam param);
    
}

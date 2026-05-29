package org.ziranziyuanting.account.service;

import org.ziranziyuanting.account.entity.SysUser;
import org.ziranziyuanting.account.param.SysUserParam;
import org.ziranziyuanting.account.param.SysUserUpdateParam;
import org.ziranziyuanting.common.service.CommonService;
import reactor.core.publisher.Mono;

public interface SysUserService extends CommonService<SysUser>
{

    Mono<String> add(SysUserParam param);

    Mono<String> updatePassword(String password);
    
    Mono<String> update(SysUserUpdateParam param);

}


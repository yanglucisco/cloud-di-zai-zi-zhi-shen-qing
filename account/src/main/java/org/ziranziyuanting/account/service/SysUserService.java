package org.ziranziyuanting.account.service;

import java.util.List;

import org.ziranziyuanting.account.entity.SysUser;
import org.ziranziyuanting.account.param.SysUserPageParam;
import org.ziranziyuanting.account.param.SysUserParam;
import org.ziranziyuanting.account.param.SysUserStatusParam;
import org.ziranziyuanting.account.param.SysUserUpdateParam;
import org.ziranziyuanting.account.vo.SysUserPageVO;
import org.ziranziyuanting.common.service.CommonService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysUserService extends CommonService<SysUser>
{

    Mono<String> add(SysUserParam param);

    Mono<String> updatePassword(String password);

    Mono<String> update(SysUserUpdateParam param);

    /**
     * 分页查询用户列表
     */
    Flux<SysUserPageVO> findUsersByPage(SysUserPageParam pageParam);

    /**
     * 统计用户数量
     */
    Mono<Long> countUsers(SysUserPageParam pageParam);

    /**
     * 更新用户状态（启用/禁用）
     */
    Mono<String> updateStatus(SysUserStatusParam param);

    /**
     * 批量逻辑删除用户
     */
    Mono<String> delete(List<Long> ids);

}


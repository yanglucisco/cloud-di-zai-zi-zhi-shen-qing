package org.ziranziyuanting.account.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ziranziyuanting.account.entity.SysUser;
import org.ziranziyuanting.account.param.SysUserParam;
import org.ziranziyuanting.account.repository.SysUserRepository;
import org.ziranziyuanting.account.service.SysUserService;
import org.ziranziyuanting.account.utils.PasswordUtil;
import org.ziranziyuanting.common.service.impl.CommonServiceImpl;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SysUserServiceImpl extends CommonServiceImpl<SysUser> implements SysUserService {
    public SysUserServiceImpl(SysUserRepository repository, PasswordEncoder passwordEncoder){
        super(repository);
    }

    @Override
    public Mono<String> add(SysUserParam param) {
        var randowmPassword = PasswordUtil.generateRandomString(param.getAccount());
        SysUser user = SysUser.builder()
            .name(param.getName())
            .gender(param.getGender())
            .orgId(param.getOrgId())
            .account(param.getAccount())
            .positionId(param.getPositionId())
            .password(PasswordUtil.generatePassword(randowmPassword))
            .directorId(param.getDirectorId())
            .build();
        return this.saveOrUpdate(user).map(u -> "添加用户成功，初始密码：" + randowmPassword);
    }
}

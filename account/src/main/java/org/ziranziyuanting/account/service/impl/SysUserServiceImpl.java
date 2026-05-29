package org.ziranziyuanting.account.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ziranziyuanting.account.config.ReactiveUserContext;
import org.ziranziyuanting.account.entity.SysUser;
import org.ziranziyuanting.account.param.SysUserParam;
import org.ziranziyuanting.account.param.SysUserUpdateParam;
import org.ziranziyuanting.account.repository.SysUserRepository;
import org.ziranziyuanting.account.service.SysUserService;
import org.ziranziyuanting.account.utils.PasswordUtil;
import org.ziranziyuanting.common.service.impl.CommonServiceImpl;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SysUserServiceImpl extends CommonServiceImpl<SysUser> implements SysUserService {
    public SysUserServiceImpl(SysUserRepository repository, PasswordEncoder passwordEncoder) {
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

    @Override
    public Mono<String> updatePassword(String password) {
        return ReactiveUserContext.getUserId().flatMap(userId -> {
            return this.findById(userId);
        })
        .flatMap(user -> {
            user.setPassword(PasswordUtil.generatePassword(password));
            return this.saveOrUpdate(user);
        }).map(u -> "修改密码成功");
    }

    @Override
    public Mono<String> update(SysUserUpdateParam param) {
        return ReactiveUserContext.getUserId().flatMap(userId -> {
            return this.findById(userId);
        }).flatMap(user -> {
            if (param.getGender() != null) user.setGender(param.getGender());
            if (param.getName() != null) user.setName(param.getName());
            if (param.getNickName() != null) user.setNickname(param.getNickName());
            if (param.getEmail() != null) user.setEmail(param.getEmail());
            if (param.getMobil() != null) user.setPhone(param.getMobil());
            if (param.getBirthday() != null) user.setBirthday(param.getBirthday());
            return this.saveOrUpdate(user);
        }).map(u -> "更新用户信息成功");
    }
}


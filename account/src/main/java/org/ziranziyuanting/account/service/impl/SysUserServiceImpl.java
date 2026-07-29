package org.ziranziyuanting.account.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ziranziyuanting.account.config.ReactiveUserContext;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.entity.SysUser;
import org.ziranziyuanting.account.param.SysUserPageParam;
import org.ziranziyuanting.account.param.SysUserParam;
import org.ziranziyuanting.account.param.SysUserStatusParam;
import org.ziranziyuanting.account.param.SysUserUpdateParam;
import org.ziranziyuanting.account.repository.SysOrgRepository;
import org.ziranziyuanting.account.repository.SysUserRepository;
import org.ziranziyuanting.account.service.SysUserService;
import org.ziranziyuanting.account.utils.PasswordUtil;
import org.ziranziyuanting.account.vo.SysUserPageVO;
import org.ziranziyuanting.common.api.BusinessException;
import org.ziranziyuanting.common.service.impl.CommonServiceImpl;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SysUserServiceImpl extends CommonServiceImpl<SysUser> implements SysUserService {
    private final SysOrgRepository sysOrgRepository;

    public SysUserServiceImpl(SysUserRepository repository, PasswordEncoder passwordEncoder,
            SysOrgRepository sysOrgRepository) {
        super(repository);
        this.sysOrgRepository = sysOrgRepository;
    }

    @Override
    public Mono<String> add(SysUserParam param) {
        SysUserRepository repo = (SysUserRepository) this.repository;
        return repo.findByAccount(param.getAccount())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new BusinessException("账号 '" + param.getAccount() + "' 已存在"));
                    }
                    SysUser user = SysUser.builder()
                            .name(param.getName())
                            .gender(param.getGender())
                            .orgId(param.getOrgId())
                            .account(param.getAccount())
                            .positionId(param.getPositionId())
                            .password(PasswordUtil.generatePassword(param.getPassword()))
                            .directorId(param.getDirectorId())
                            .userStatus("ENABLE")
                            .build();
                    return this.saveOrUpdate(user)
                            .map(u -> "添加用户成功");
                });
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
            if (param.getAvatar() != null) user.setAvatar(param.getAvatar());
            return this.saveOrUpdate(user);
        }).map(u -> "更新用户信息成功");
    }

    @Override
    public Flux<SysUserPageVO> findUsersByPage(SysUserPageParam pageParam) {
        SysUserRepository repo = (SysUserRepository) this.repository;
        PageRequest pageRequest = PageRequest.of(pageParam.getPage(), pageParam.getPageSize());
        return repo.findUsersByPage(pageParam.getKeyword(), pageParam.getStatus(), pageParam.getOrgId(), pageRequest)
                .collectList()
                .flatMapMany(users -> {
                    List<Long> orgIds = users.stream()
                            .map(SysUser::getOrgId)
                            .filter(Objects::nonNull)
                            .distinct()
                            .collect(Collectors.toList());
                    Mono<Map<Long, String>> orgNameMapMono;
                    if (orgIds.isEmpty()) {
                        orgNameMapMono = Mono.just(Collections.emptyMap());
                    } else {
                        orgNameMapMono = sysOrgRepository.findAllById(orgIds)
                                .collectMap(SysOrg::getId, SysOrg::getName);
                    }
                    return orgNameMapMono.flatMapMany(orgNameMap ->
                            Flux.fromIterable(users).map(user -> SysUserPageVO.builder()
                                    .id(user.getId() != null ? user.getId().toString() : null)
                                    .avatar(user.getAvatar())
                                    .account(user.getAccount())
                                    .name(user.getName())
                                    .gender(user.getGender())
                                    .phone(user.getPhone())
                                    .orgName(orgNameMap.getOrDefault(user.getOrgId(), ""))
                                    .userStatus(user.getUserStatus())
                                    .build()));
                });
    }

    @Override
    public Mono<Long> countUsers(SysUserPageParam pageParam) {
        SysUserRepository repo = (SysUserRepository) this.repository;
        return repo.countUsers(pageParam.getKeyword(), pageParam.getStatus(), pageParam.getOrgId());
    }

    @Override
    public Mono<String> updateStatus(SysUserStatusParam param) {
        return this.findById(param.getId())
                .switchIfEmpty(Mono.error(new BusinessException("用户不存在")))
                .flatMap(user -> {
                    user.setUserStatus(param.getStatus());
                    return this.saveOrUpdate(user);
                })
                .map(u -> "更新用户状态成功");
    }
}


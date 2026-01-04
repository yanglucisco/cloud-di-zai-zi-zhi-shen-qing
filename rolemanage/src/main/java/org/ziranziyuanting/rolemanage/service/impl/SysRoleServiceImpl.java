package org.ziranziyuanting.rolemanage.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ziranziyuanting.common.service.impl.CommonServiceImpl;
import org.ziranziyuanting.rolemanage.entity.SysRole;
import org.ziranziyuanting.rolemanage.respository.SysRoleRepository;
import org.ziranziyuanting.rolemanage.service.SysRoleService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SysRoleServiceImpl extends CommonServiceImpl<SysRole> implements SysRoleService {
    public SysRoleServiceImpl(SysRoleRepository repository, PasswordEncoder passwordEncoder){
        super(repository);
    }
}

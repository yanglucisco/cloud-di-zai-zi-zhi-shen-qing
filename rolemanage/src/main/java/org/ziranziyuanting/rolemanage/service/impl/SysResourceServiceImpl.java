package org.ziranziyuanting.rolemanage.service.impl;

import org.springframework.stereotype.Service;
import org.ziranziyuanting.common.service.impl.CommonServiceImpl;
import org.ziranziyuanting.rolemanage.entity.SysResource;
import org.ziranziyuanting.rolemanage.respository.SysResourceRepository;
import org.ziranziyuanting.rolemanage.service.SysResourceService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SysResourceServiceImpl extends CommonServiceImpl<SysResource> implements SysResourceService {
    public SysResourceServiceImpl(SysResourceRepository repository){
        super(repository);
    }
}

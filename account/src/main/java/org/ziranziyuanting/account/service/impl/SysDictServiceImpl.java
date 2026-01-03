package org.ziranziyuanting.account.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.ziranziyuanting.account.entity.SysDict;
import org.ziranziyuanting.account.entity.SysOrg;
import org.ziranziyuanting.account.param.AddOrgParam;
import org.ziranziyuanting.account.repository.SysDictRepository;
import org.ziranziyuanting.account.repository.SysOrgRepository;
import org.ziranziyuanting.account.service.SysDictService;
import org.ziranziyuanting.account.service.SysOrgService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SysDictServiceImpl extends CommonServiceImpl<SysDict> implements SysDictService {
    public SysDictServiceImpl(SysDictRepository repository){
        super(repository);
    }
}

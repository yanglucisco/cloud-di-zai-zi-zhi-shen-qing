package org.ziranziyuanting.account.service.impl;

import org.springframework.stereotype.Service;
import org.ziranziyuanting.account.entity.SysPosition;
import org.ziranziyuanting.account.repository.SysPositionRepository;
import org.ziranziyuanting.account.service.SysPositionService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SysPositionServiceImpl extends CommonServiceImpl<SysPosition> implements SysPositionService {
    public SysPositionServiceImpl(SysPositionRepository repository){
        super(repository);
    }
}

package org.ziranziyuanting.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.ziranziyuanting.account.entity.CommonEntity;
import org.ziranziyuanting.account.repository.CommonReactiveCrudRepository;
import org.ziranziyuanting.account.service.CommonService;
import org.ziranziyuanting.common.CommonSnowflake;

import reactor.core.publisher.Mono;

public class CommonServiceImpl<T extends CommonEntity> implements CommonService<T> {
    private final CommonReactiveCrudRepository<T> repository;
    @Autowired
    private CommonSnowflake snowflake;
    public CommonServiceImpl(CommonReactiveCrudRepository<T> repository) {
        this.repository = repository;
    }
    public Mono<T> saveWithCheck(T t) {
        if(t.getId() == null) {
            t.setId(snowflake.nextId());
            t.setNew(true);
            return repository.save(t);
        }
        t.setNew(false);
        return repository.save(t);
    }
}

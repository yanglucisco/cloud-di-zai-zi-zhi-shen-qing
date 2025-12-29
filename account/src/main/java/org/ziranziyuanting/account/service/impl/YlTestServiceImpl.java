package org.ziranziyuanting.account.service.impl;

import org.springframework.stereotype.Service;
import org.ziranziyuanting.account.entity.YlTest;
import org.ziranziyuanting.account.repository.YlTestRepository;
import org.ziranziyuanting.account.service.YlTestService;

import reactor.core.publisher.Flux;

@Service
public class YlTestServiceImpl implements YlTestService {
    private final YlTestRepository repository;
    public YlTestServiceImpl(YlTestRepository repository){
        this.repository = repository;
    }
    @Override
    public Flux<YlTest> all(){
        return repository.findAll();
    }
}

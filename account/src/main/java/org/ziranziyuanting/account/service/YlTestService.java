package org.ziranziyuanting.account.service;

import org.ziranziyuanting.account.entity.YlTest;

import reactor.core.publisher.Flux;

public interface YlTestService {

    Flux<YlTest> all();
    
}

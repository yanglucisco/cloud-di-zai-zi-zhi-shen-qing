package org.ziranziyuanting.account.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.ziranziyuanting.account.entity.YlTest;

public interface YlTestRepository extends ReactiveCrudRepository<YlTest, String>{
    
}

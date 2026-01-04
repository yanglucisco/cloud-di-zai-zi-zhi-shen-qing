package org.ziranziyuanting.common.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.ziranziyuanting.common.entity.CommonEntity;

public abstract interface CommonReactiveCrudRepository<T extends CommonEntity> 
extends ReactiveCrudRepository<T, Long> 
{
    // @SuppressWarnings("null")
    // default Mono<T> saveWithCheck(T t) {
    //     return existsById(t.getId())
    //         .flatMap(exists -> {
    //             if (exists) {
    //                 // 数据库中已存在
    //                 t.setNew(false);
    //             } else if (!exists) {
    //                 // 数据库中不存在
    //                 t.setNew(true);
    //             }
    //             return save(t);
    //         });
    // }
}

package org.ziranziyuanting.common.service;

import org.reactivestreams.Publisher;
import org.ziranziyuanting.common.entity.CommonEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract interface CommonService<T extends CommonEntity> {
    Mono<T> saveOrUpdate(T t);
    
    <S extends T> Mono<S> save(S entity);

    <S extends T> Flux<S> saveAll(Iterable<S> entities);

    <S extends T> Flux<S> saveAll(Publisher<S> entityStream);

    Mono<T> findById(Long id);

    Mono<T> findById(Publisher<Long> id);

    Mono<Boolean> existsById(Long id);

    Mono<Boolean> existsById(Publisher<Long> id);

    Flux<T> findAll();

    Flux<T> findAllById(Iterable<Long> ids);

    Flux<T> findAllById(Publisher<Long> idStream);

    Mono<Long> count();

    Mono<Void> deleteById(Long id);

    Mono<Void> deleteById(Publisher<Long> id);

    Mono<Void> delete(T entity);

    Mono<Void> deleteAllById(Iterable<? extends Long> ids);

    Mono<Void> deleteAll(Iterable<? extends T> entities);

    Mono<Void> deleteAll(Publisher<? extends T> entityStream);

    Mono<Void> deleteAll();
}


package org.ziranziyuanting.common.service.impl;

import java.time.LocalDateTime;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.ziranziyuanting.common.entity.CommonEntity;
import org.ziranziyuanting.common.repository.CommonReactiveCrudRepository;
import org.ziranziyuanting.common.service.CommonService;
import org.ziranziyuanting.common.CommonSnowflake;
import org.ziranziyuanting.common.config.ReactiveSecurityContext;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class CommonServiceImpl<T extends CommonEntity> implements CommonService<T> {
    private final CommonReactiveCrudRepository<T> repository;
    @Autowired
    private CommonSnowflake snowflake;

    public CommonServiceImpl(CommonReactiveCrudRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public Mono<T> saveOrUpdate(T t) {
        return ReactiveSecurityContext.getCurrentUserId().flatMap(userId -> {
            if (t.getId() == null) {
                t.setId(snowflake.nextId());
                t.setNew(true);
                t.setCreateTime(LocalDateTime.now());
                t.setCreateUser(userId);
                return repository.save(t);
            }
            t.setNew(false);
            t.setUpdateTime(LocalDateTime.now());
            t.setUpdateUser(userId);
            return repository.save(t);
        });
    }

    @SuppressWarnings("null")
    @Override
    public <S extends T> Mono<S> save(S entity) {
        return repository.save(entity);
    }

    @SuppressWarnings("null")
    @Override
    public <S extends T> Flux<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    @SuppressWarnings("null")
    @Override
    public <S extends T> Flux<S> saveAll(Publisher<S> entityStream) {
        return repository.saveAll(entityStream);
    }

    @SuppressWarnings("null")
    @Override
    public Mono<T> findById(Long id) {
        return repository.findById(id);
    }

    @SuppressWarnings("null")
    @Override
    public Mono<T> findById(Publisher<Long> id) {
        return repository.findById(id);
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Boolean> existsById(Long id) {
        return repository.existsById(id);
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Boolean> existsById(Publisher<Long> id) {
        return repository.existsById(id);
    }

    @SuppressWarnings("null")
    @Override
    public Flux<T> findAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    @Override
    public Flux<T> findAllById(Iterable<Long> ids) {
        return repository.findAllById(ids);
    }

    @SuppressWarnings("null")
    @Override
    public Flux<T> findAllById(Publisher<Long> idStream) {
        return repository.findAllById(idStream);
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Long> count() {
        return repository.count();
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Void> deleteById(Publisher<Long> id) {
        return repository.deleteById(id);
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Void> delete(T entity) {
        return repository.delete(entity);
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Void> deleteAllById(Iterable<? extends Long> ids) {
        return repository.deleteAllById(ids);
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Void> deleteAll(Iterable<? extends T> entities) {
        return repository.deleteAll(entities);
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Void> deleteAll(Publisher<? extends T> entityStream) {
        return repository.deleteAll(entityStream);
    }

    @SuppressWarnings("null")
    @Override
    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }
}

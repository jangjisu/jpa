package com.example.connect.api.domain;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class CreateRepositoryImpl <T> implements CreateRepository<T> {
    private final EntityManager em;

    @Override
    public <S extends T> S create(S entity) {
        Assert.notNull(entity, "Entity must not be null");
        em.persist(entity);
        return entity;
    }

    @Override
    public <S extends T> List<S> createAll(Iterable<S> entities) {
        Assert.notNull(entities, "Entities must not be null");
        List<S> result = new ArrayList<>();

        for(S entity : entities) {
            result.add(this.create(entity));
        }

        return result;
    }

    @Override
    public <S extends T> S createAndFlush(S entity) {
        S result = this.create(entity);
        em.flush();
        return result;
    }

    @Override
    public <S extends T> List<S> createAllAndFlush(Iterable<S> entities) {
        List<S> result = this.createAll(entities);
        em.flush();
        return result;
    }
}

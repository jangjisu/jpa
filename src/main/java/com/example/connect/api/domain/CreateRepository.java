package com.example.connect.api.domain;

import java.util.List;

public interface CreateRepository<T> {
    <S extends T> S create(S entity);

    <S extends T> List<S> createAll(Iterable<S> entities);

    <S extends T> S createAndFlush(S entity);

    <S extends T> List<S> createAllAndFlush(Iterable<S> entities);
}
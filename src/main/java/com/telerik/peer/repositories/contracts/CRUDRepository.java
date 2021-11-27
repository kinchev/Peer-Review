package com.telerik.peer.repositories.contracts;

import java.util.List;

public interface CRUDRepository<T> {

    void delete(long id);

    void create(T entity);

    void update(T entity);

    List<T> getAll();

    T getById(long id);

    <V> T getByField(String fieldName, V value);
}



